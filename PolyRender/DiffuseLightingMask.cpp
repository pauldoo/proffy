#include "stdafx.h"
#include "DiffuseLightingMask.h"
#include "RenderInfo.h"
#include "Intersect.h"
#include "Light.h"
#include "World.h"
#include "Norms.h"

#include "TimingPool.h"

Color DiffuseLightingMask(const Auto<const RenderInfo>& info, const Auto<const Light>& light) {
	const Point point2light = Normalize(light->m_position - info->m_intersect->PositionAt());
	const Real attentuationParameter = 1/NormSquared(point2light);
	return DotProd(info->m_intersect->NormalAt(), point2light) * attentuationParameter * light->m_color;
}
