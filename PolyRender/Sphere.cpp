#include "stdafx.h"
#include "Sphere.h"
#include "ColorSolid.h"
#include "Polynomial.h"
#include "Vector3d.h"
#include "Line.h"
#include "Maybe.h"
#include "SolidIntersect.h"
#include "Utilities.h"

#include "TimingPool.h"

namespace {
	class Sphere : public ColorSolid {
	public:
		Sphere(const Real radius) : m_radius(radius) {}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(const Line& lightRay, const eIntersectType, RenderMemory&) const 
		{
			TIMETHISFUNCTION;
			const Point lightPosition = lightRay.Start();
			const Point lightDirection = lightRay.Direction();

			const Real a = DotProd(lightDirection, lightDirection);
			const Real b = 2*DotProd(lightDirection, lightPosition);
			const Real c = DotProd(lightPosition, lightPosition) - m_radius * m_radius;
			
			const Maybe<Real> distance = FindSmallestPositiveRootOfQuadratic(a,b,c);
			if (distance.IsValid())
				return Intersect00(new SolidIntersect(this, lightRay, distance.Get()));
			else 
				return Intersect00();
		}

		// Solid
		bool InsideQ(const Point& point) const {
			return DotProd(point,point) <= m_radius*m_radius;
		}

		// ColorSolid
		Point NormalAt(const Point& point) const {
			return point/m_radius;
		}
		
		// ColorSolid
		Point UVCoordsAt(const Point& point) const {
			TIMETHISFUNCTION;
			const Real x = point.X();
			const Real y = point.Y();
			const Real z = point.Z();
			const Real longitude = (x == 0 && z == 0) ? 0 : atan2(z,x);
			const Real latitude = (x == 0 && y == 0 && z == 0) ? 0 : atan2(y, sqrt(x*x + z*z));
			return Point(longitude, latitude, 0);
		}

	private:
		const Real m_radius;
	};
}

Auto<const Solid> MakeSphere(const Real radius) {
	return new Sphere(radius);
}
