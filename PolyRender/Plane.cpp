#include "stdafx.h"
#include "Plane.h"
#include "ColorSolid.h"
#include "Line.h"
#include "SolidIntersect.h"
#include "Maybe.h"
#include "TimingPool.h"

namespace {
	class Plane : public ColorSolid {
	public:
		// Solid
		Intersect00 DetermineClosestIntersectionPoint(const Line& lightRay,const eIntersectType,RenderMemory&) const 
		{
			TIMETHISFUNCTION;
			const Real distance = -lightRay.Start().Z() / lightRay.Direction().Z();
			if (distance >= 0) return Intersect00(new SolidIntersect(this, lightRay, distance));
			else return Intersect00();
		}

		// Solid
		bool InsideQ(const Point& point) const {
			return point.Z() >= 0;
		}

		// ColorSolid
		Point NormalAt(const Point&) const {
			return Point(0,0,1);
		}

		// ColorSolid
		Point UVCoordsAt(const Point& point) const {
			return point;
		}
	};
}

Auto<const Solid> MakePlane() {
	return new Plane();
}