#pragma once

#include "Auto.h"
#include "Vector3D.h"
#include "Intersect.h"

class Line;
class ColorSolid;

class SolidIntersect : public Intersect {
public:
	SolidIntersect(const Auto<const ColorSolid>& solid, const Line& lightRay, double distance);

	// Intersect
	Real DistanceAt() const;

	// Intersect
	Color Render(const Auto<const RenderInfo>&, RenderMemory& renderMemory) const;

	// Intersect
	Point NormalAt() const;
	
	// Intersect
	Point UVCoordsAt() const;

	// Intersect
	Point PositionAt() const;

private:
	const Auto<const ColorSolid> m_solid;
	const Real m_distance;
	const Point m_point;
};