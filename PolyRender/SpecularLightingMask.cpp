#include "stdafx.h"
#include "SpecularLightingMask.h"
#include "RenderInfo.h"
#include "Intersect.h"
#include "World.h"
#include "Light.h"
#include "TimingPool.h"

Color SpecularLightingMask(const Auto<const RenderInfo>& info, const Auto<const Light>& light) {
	const Point position = info->m_intersect->PositionAt();
	const Point normal = info->m_intersect->NormalAt();
	const Point eye = info->m_world->GetProjectionPlane().ProjectionPoint();
	const Point point2eye = Normalize(eye - position);
		
	const Point point2light = Normalize(light->m_position - position);
	const Point halfAngle = Normalize((point2eye + point2light)/2);
	const Real angle = std::acos(DotProd(halfAngle, normal));
	const Real specularFactor = std::exp(-std::pow(angle/.05,2));
	return 20 * light->m_color * specularFactor;
}
