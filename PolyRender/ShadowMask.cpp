#include "stdafx.h"
#include "ShadowMask.h"
#include "RenderInfo.h"
#include "Intersect.h"
#include "Solid.h"
#include "Maybe.h"
#include "World.h"
#include "Light.h"

#include "TimingPool.h"

bool InShadow(const Auto<const RenderInfo>& info, const Auto<const Light>& light, RenderMemory& renderMemory) {
	TIMETHISFUNCTION;
	const Point position = info->m_intersect->PositionAt();

	const Point point2light = light->m_position - position;


	Intersect00 tempIntersect;
	{
		TIMETHISBLOCK("Intersecting")
		tempIntersect = 
			info->m_world->GetSolid()->DetermineClosestIntersectionPoint(
				Line(position + .001*(Normalize(point2light)), point2light),
				eShadow,
				renderMemory);
	}

	return (tempIntersect.IsValid() && (tempIntersect.Get()->DistanceAt() <= 1 && tempIntersect.Get()->DistanceAt() > 0));
}
