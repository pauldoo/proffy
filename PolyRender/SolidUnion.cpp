#include "stdafx.h"
#include "SolidUnion.h"
#include "Intersect.h"
#include "Maybe.h"
#include "Solid.h"
#include "Auto.h"
#include "TimingPool.h"

class SolidUnion : public Solid {
public:
	SolidUnion(const Auto<const Solid>& solidA, const Auto<const Solid>& solidB)
	:	m_solidA(solidA),
		m_solidB(solidB)
	{
	}

	// Solid
	Intersect00 DetermineClosestIntersectionPoint(
		const Line& lightRay, 
		const eIntersectType intersectionType,
		RenderMemory& renderMemory
	) const {
		TIMETHISFUNCTION;
		Intersect00 intersectA = m_solidA->DetermineClosestIntersectionPoint(lightRay, intersectionType, renderMemory);
		Intersect00 intersectB = m_solidB->DetermineClosestIntersectionPoint(lightRay, intersectionType, renderMemory);
		
		// TODO: remove code duplication with SolidIntersection
		if (intersectA.IsValid() && intersectB.IsValid()) {
			if (intersectA.Get()->DistanceAt() < intersectB.Get()->DistanceAt()) 
				return intersectA;
			else 
				return intersectB;
		} else if (intersectA.IsValid()) {
			return intersectA;
		} else if (intersectB.IsValid()) {
			return intersectB;
		}
		return Intersect00();
	}

	// Solid
	bool InsideQ(const Point& point) const {
		return m_solidA->InsideQ(point) || m_solidB->InsideQ(point);
	}

private:
	const Auto<const Solid> m_solidA, m_solidB;
};

Auto<const Solid> MakeSolidUnion(const Auto<const Solid>& solidA, const Auto<const Solid>& solidB) {
	return new SolidUnion(solidA,solidB);
}