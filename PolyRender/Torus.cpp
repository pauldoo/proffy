#include "stdafx.h"
#include "Torus.h"
#include "ColorSolid.h"
#include "Polynomial.h"
#include "LineFuncs.h"
#include "Line.h"
#include "Maybe.h"
#include "SolidIntersect.h"
#include "BoundingSolid.h"
#include "SolidIntersection.h"
#include "TransformationAdapter.h"
#include "Plane.h"
#include "Sphere.h"
#include "Fold.h"
#include "BairstowPolySolve.h"
#include "TimingPool.h"

// TODO: still no test for torus

namespace {
	class BairstowMemory : public LinkCount {
	public:
		std::vector<std::pair<Real, Real> > m_remember;
	};

	class Torus : public ColorSolid {
	public:
		Torus(const Real innerRadius, const Real outerRadius) 
		:	m_innerRadius(innerRadius), m_outerRadius(outerRadius) {}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(
			const Line& lightRay,
			const eIntersectType intersectType,
			RenderMemory& renderMemory) const 
		{
			TIMETHISFUNCTION;
			Auto<LinkCount>& a = renderMemory.Guess(IntersectID(intersectType, this));
			if (a.Pointer() == NULL) {
				a = new BairstowMemory;
			}

			const Maybe<Real>(root) = BairstowPolySolve::FindSmallestPositiveRoot(
				CartesionEquation(lightRay),
				dynamic_cast<BairstowMemory*>(a.Pointer())->m_remember);

			if (root.IsValid()) return Intersect00(new SolidIntersect(this, lightRay, root.Get()));
			else return Intersect00();
		}

		// Solid
		bool InsideQ(const Point& point) const {
			TIMETHISFUNCTION;
			return CartesionEquation(
				Line(point, Point(0,0,0))
			).EvaluateAt<Real>(0) < 0;
		}

		// ColorSolid
		Point NormalAt(const Point& point) const {
			TIMETHISFUNCTION;
			const Point toRim = Point(point.X(), point.Y(),0);
			const Point fromRim = point - Normalize(toRim) * m_outerRadius;
			return Normalize(fromRim);

			// TODO: This is a generic way to find the normal of a cartesian equation.
			/*const RPolynomial t = RPolynomial::MakeT();
			const Real& x = point.X();
			const Real& y = point.Y();
			const Real& z = point.Z();
			return Point(
				PartialDerivativeHelper(t,RPolynomial(y),RPolynomial(z),x),
				PartialDerivativeHelper(RPolynomial(x),t,RPolynomial(z),y),
				PartialDerivativeHelper(RPolynomial(x),RPolynomial(y),t,z)
			).Normalize();*/
		}
		
		// ColorSolid
		Point UVCoordsAt(const Point& point) const {
			TIMETHISFUNCTION;
			const Real x = point.X();
			const Real y = point.Y();
			const Real z = point.Z();
			return Point(atan2(x,y), atan2(.5 - sqrt(x*x+y*y),-z), 0);
		}

	private:
		/*Real PartialDerivativeHelper(const RPolynomial& x, const RPolynomial& y, const RPolynomial& z, const Real at) const  {
			return CartesionEquation(ParametricLine(x, y, z)).Differentiate().EvaluateAt(at);
		}*/

		RPolynomial CartesionEquation(const Line& line) const {
			// TODO: Matixprod must be implemented over Line to make this work perfectly.
			/*const RPolynomial x = line.X();
			const RPolynomial y = line.Y();
			const RPolynomial z = line.Z();
			const RPolynomial temp1 = x*x + y*y;
			const RPolynomial temp2 = temp1 + z*z + (m_outerRadius * m_outerRadius - m_innerRadius * m_innerRadius);
			const RPolynomial result2 = temp2 * temp2 - temp1 * (m_outerRadius * m_outerRadius * 4);*/
			TIMETHISFUNCTION;
			const Real 
				ax = line.Start().X(),
				bx = line.Direction().X(),
				ay = line.Start().Y(),
				by = line.Direction().Y(),
				az = line.Start().Z(),
				bz = line.Direction().Z(),
				R = m_outerRadius,
				r = m_innerRadius;
			
			const Real 
				c0 = ax*ax + ay*ay,
				c1 = 2*(ax*bx + ay*by),
				c2 = bx*bx + by*by,
				g = R*R - r*r;
			
			const Real 
				d0 = c0 + az*az + g,
				d1 = c1 + 2*az*bz,
				d2 = c2 + bz*bz;
			
			const Real
				e0 = d0*d0 - 4*c0*R*R,
				e1 = 2*d0*d1 - 4*c1*R*R,
				e2 = d1*d1 - 4*c2*R*R + 2*d0*d2,
				e3 = 2*d1*d2,
				e4 = d2*d2;
			
			RPolynomial result(0, 4);
			result[0]=e0;
			result[1]=e1;
			result[2]=e2;
			result[3]=e3;
			result[4]=e4;
			return result;
		}
			

		const Real m_innerRadius;
		const Real m_outerRadius;
	};
}

Auto<const Solid> MakeTorus(const Real innerRadius, const Real outerRadius) {
	return MakeBoundingSolid(
		Fold(MakeSolidIntersection,
			MakeSphere(innerRadius + outerRadius),
			MakeTransformationAdapter(
				MakePlane(),
				IdentityMatrix(),
				Point(0,0,-1)
			),
			MakeTransformationAdapter(
				MakePlane(),
				Matrix(Point(1,0,0), Point(0,-1,0), Point(0,0,-1)),
				Point(0,0,1)
			)
		),
		new Torus(innerRadius, outerRadius)
	);
}