#include "stdafx.h"
#include "SolidIntersection.h"
#include "Intersect.h"
#include "Vector3d.h"
#include "Maybe.h"
#include "Solid.h"
#include "Auto.h"
#include "TimingPool.h"

namespace {
	class SolidIntersection : public Solid {
	public:
		SolidIntersection(const Auto<const Solid>& solidA, const Auto<const Solid>& solidB)
		:	m_solidA(solidA),
			m_solidB(solidB)
		{
		}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(
			const Line& lightRay,
			const eIntersectType intersectionType,
			RenderMemory& renderMemory) const 
		{
			TIMETHISFUNCTION;
			Intersect00 intersectA = m_solidA->DetermineClosestIntersectionPoint(lightRay,intersectionType,renderMemory);
			Intersect00 intersectB = m_solidB->DetermineClosestIntersectionPoint(lightRay,intersectionType,renderMemory);
			
			const bool AisValid = intersectA.IsValid() && m_solidB->InsideQ(intersectA.Get()->PositionAt());
			const bool BisValid = intersectB.IsValid() && m_solidA->InsideQ(intersectB.Get()->PositionAt());

			if (AisValid && BisValid) {
				if (intersectA.Get()->DistanceAt() < intersectB.Get()->DistanceAt()) 
					return intersectA;
				else 
					return intersectB;
			} else if (AisValid) {
				return intersectA;
			} else if (BisValid) {
				return intersectB;
			}
			return Intersect00();
		}


		// Solid
		bool InsideQ(const Point& point) const {
			return m_solidA->InsideQ(point) && m_solidB->InsideQ(point);
		}

	private:
		const Auto<const Solid> m_solidA, m_solidB;
	};
}

Auto<const Solid> MakeSolidIntersection(const Auto<const Solid>& solidA, const Auto<const Solid>& solidB) {
	return new SolidIntersection(solidA,solidB);
}