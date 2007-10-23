#include "stdafx.h"
#include "ApplyTexture.h"
#include "ColorDeclarations.h"
#include "Maybe.h"
#include "Solid.h"
#include "IntersectForwarder.h"
#include "Point.h"
#include "RenderMask.h"
#include "Auto.h"

class RenderInfo;

namespace {
	class ApplyTextureIntersect : public IntersectForwarder {
	public:
		ApplyTextureIntersect(
			const Auto<const RenderMask>& renderMask, 
			const Auto<const Intersect>& intersect
		) :	IntersectForwarder(intersect),
			m_renderMask(renderMask)
		{
		}

		// Intersect
		Color Render(const Auto<const RenderInfo>& info, RenderMemory& renderMemory) const {
			return m_renderMask->Render(info, renderMemory);
		}

	private:
		const Auto<const RenderMask> m_renderMask;
	};

	class ApplyTexture : public Solid {
	public:
		ApplyTexture(
			const Auto<const Solid>& solid, 
			const Auto<const RenderMask>& renderMask
		) :	m_solid(solid),
			m_renderMask(renderMask)
		{
		}

		// Solid
		Intersect00 DetermineClosestIntersectionPoint(
			const Line& lightRay,
			const eIntersectType intersectionType,
			RenderMemory& renderMemory) const 
		{
			const Intersect00 intersect = m_solid->DetermineClosestIntersectionPoint(lightRay, intersectionType, renderMemory);
			if(intersect.IsValid()) {
				return Intersect00(new ApplyTextureIntersect(m_renderMask, intersect.Get()));
			} else {
				return intersect;
			}
		}

		// Solid
		bool InsideQ(const Point& point) const {
			return m_solid->InsideQ(point);
		}

	private:
		const Auto<const Solid> m_solid;
		const Auto<const RenderMask> m_renderMask;
	};
}

Auto<const Solid> MakeApplyTexture(const Auto<const Solid>& solid, const Auto<const RenderMask>& renderMask) {
	return new ApplyTexture(solid, renderMask);
}