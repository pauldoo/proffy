#pragma once
#include "Solid.h"
#include "ColorDeclarations.h"
#include "PointDeclarations.h"

class RenderInfo;

class ColorSolid : public Solid {
public:
	virtual Color Render(const Auto<const RenderInfo>&, RenderMemory& renderMemory) const;
	virtual Point NormalAt(const Point&) const = 0;
	virtual Point UVCoordsAt(const Point&) const = 0;
};
