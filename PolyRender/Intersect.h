#pragma once
#include "LinkCount.h"
#include "ColorDeclarations.h"
#include "PointDeclarations.h"

class RenderInfo;
class RenderMemory;

class Intersect : public LinkCount {
public:
	virtual Real DistanceAt() const = 0;
	virtual Color Render(const Auto<const RenderInfo>&, RenderMemory&) const = 0;
	virtual Point NormalAt() const = 0;
	virtual Point UVCoordsAt() const = 0;
	virtual Point PositionAt() const = 0;
};
