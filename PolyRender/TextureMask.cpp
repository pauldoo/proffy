#include "stdafx.h"
#include "TextureMask.h"
#include "RenderInfo.h"
#include "Intersect.h"
#include "TextureMap.h"

Color TextureMask(const Auto<const RenderInfo>& info, const Auto<const TextureMap>& textureMap) {
	const Point uvCoords = info->m_intersect->UVCoordsAt();
	return textureMap->ColorAt(uvCoords.X(), uvCoords.Y());
}
