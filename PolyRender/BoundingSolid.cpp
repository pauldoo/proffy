#include "stdafx.h"
#include "BoundingSolid.h"
#include "Maybe.h"
#include "Line.h"
#include "Solid.h"
#include "Auto.h"
#include "TimingPool.h"

namespace {
	class BoundingSolid : public Solid {
	public:
		BoundingSolid(const Auto<const Solid>& bound, const Auto<const Solid>& solid) 
		: 	m_bound(bound), m_solid(solid)
		{
		}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(
			const Line& lightRay,
			const eIntersectType intersectionType,
			RenderMemory& renderMemory) const 
		{
			Intersect00 boundIntersect = m_bound->DetermineClosestIntersectionPoint(
				lightRay,intersectionType,renderMemory);
			
			if (boundIntersect.IsValid() || m_bound->InsideQ(lightRay.Start())) {
				const Intersect00 intersect = m_solid->DetermineClosestIntersectionPoint(
					lightRay,intersectionType,renderMemory);

				// unnessesary checking here
				/*if (intersect.IsValid()) {
					if (!m_bound->InsideQ(intersect.Get()->PositionAt()))
						throw Exception("The solid should always lie inside the solid");
				}*/
				// also we have a problem if the lightRay originates from inside the solid

				return intersect;
			}
			return Intersect00();
		}

		// Solid
		bool InsideQ(const Point& point) const {
			return m_bound->InsideQ(point) && m_solid->InsideQ(point);
		}

	private:
		const Auto<const Solid> m_bound, m_solid;
	};
}

Auto<const Solid> MakeBoundingSolid(const Auto<const Solid>& bound, const Auto<const Solid>& solid) {
	return new BoundingSolid(bound, solid);
}
