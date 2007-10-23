#pragma once
#include "LinkCount.h"
#include "PointDeclarations.h"
#include "AutoDeclarations.h"
#include "MaybeDeclarations.h"

#include "RenderMemory.h"

class Line;
class Intersect;

typedef Maybe<Auto<const Intersect> > Intersect00;

class Solid : public LinkCount {
public:
	virtual Intersect00 DetermineClosestIntersectionPoint(
		const Line& lightRay,
		const eIntersectType renderType,
		RenderMemory& renderMemory) const = 0;

	virtual bool InsideQ(const Point& point) const = 0;
};
