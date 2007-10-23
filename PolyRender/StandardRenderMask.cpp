#include "stdafx.h"
#include "StandardRenderMask.h"

#include "RenderMask.h"
#include "RenderInfo.h"
#include "World.h"
#include "ShadowMask.h"
#include "TextureMask.h"
#include "DiffuseLightingMask.h"
#include "SpecularLightingMask.h"

#include "TimingPool.h"

namespace {
	Color PieseWizeTimes(const Color& a, const Color& b) {
		return Color(a.X() * b.X(), a.Y() * b.Y(), a.Z() * b.Z());
	}

	class StandardRenderMask : public RenderMask {
	public:
		StandardRenderMask(const Auto<const TextureMap>& textureMap)
		 : m_textureMap(textureMap) {}

		/// RenderMask
		Color Render(const Auto<const RenderInfo>& info, RenderMemory& renderMemory) const {
			Color result;
			const World::LightList& lights = info->m_world->GetLights();
			for (World::LightList::const_iterator light = lights.begin() ; light != lights.end() ; light++) {
				if (!InShadow(info, *light, renderMemory)) {
					result += PieseWizeTimes(TextureMask(info, m_textureMap), DiffuseLightingMask(info, *light)) + SpecularLightingMask(info, *light);
				}
			}
			return result;
		}
	
	private:
		Auto<const TextureMap> m_textureMap;
	};
}

Auto<const RenderMask> MakeStandardRenderMask(const Auto<const TextureMap>& textureMap) {
	return new StandardRenderMask(textureMap);
}