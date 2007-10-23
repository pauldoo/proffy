#include "stdafx.h"
#include "BumpMapAdapter.h"
#include "Line.h"
#include "Maybe.h"
#include "Solid.h"
#include "Auto.h"
#include "BumpMap.h"
#include "Auto.h"
#include "IntersectForwarder.h"

namespace {
	class BumpMapAdapterIntersect : public IntersectForwarder {
	public:
		BumpMapAdapterIntersect(
			const Auto<const BumpMap>& bumpMap, 
			const Auto<const Intersect>& intersect
		) :	m_bumpMap(bumpMap),
			IntersectForwarder(intersect)
		{
		}

		// Intersect
		Point NormalAt() const {
			const Point uvCoords = m_intersect->UVCoordsAt();
			return MatrixProd(m_bumpMap->CoordSysAt(uvCoords), m_intersect->NormalAt());
		}

	private:
		const Auto<const BumpMap> m_bumpMap;
	};

	class BumpMapAdapter : public Solid {
	public:
		friend class BumpMapAdapterIntersect;

		BumpMapAdapter(const Auto<const Solid>& solid, const Auto<const BumpMap>& bumpMap)
		:	m_solid(solid),
			m_bumpMap(bumpMap)
		{
		}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(
			const Line& lightRay, 
			const eIntersectType intersectionType,
			RenderMemory& renderMemory) const 
		{
			const Intersect00 intersect = m_solid->DetermineClosestIntersectionPoint(
				lightRay,
				intersectionType,
				renderMemory);
			
			// TODO: code duplication needs to be removed (introduction of NULL intersect maybe?)
			if (intersect.IsValid())
				return Intersect00(new BumpMapAdapterIntersect(m_bumpMap, intersect.Get()));
			else 
				return intersect;
		}

		// Solid
		bool InsideQ(const Point& point) const {
			return m_solid->InsideQ(point);
		}

	private:
		const Auto<const Solid> m_solid;
		const Auto<const BumpMap> m_bumpMap;
	};
}

Auto<const Solid> MakeBumpMapAdapter(const Auto<const Solid>& solid, const Auto<const BumpMap>& bumpMap) {
	return new BumpMapAdapter(solid, bumpMap);
}