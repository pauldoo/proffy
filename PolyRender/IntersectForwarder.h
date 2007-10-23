#pragma once

#include "Auto.h"
#include "Intersect.h"

class IntersectForwarder : public Intersect {
public:
	IntersectForwarder(const Auto<const Intersect>& intersect);

	// Intersect
	Real DistanceAt() const;

	// Intersect
	Color Render(const Auto<const RenderInfo>&, RenderMemory&) const;

	// Intersect
	Point NormalAt() const;
	
	// Intersect
	Point UVCoordsAt() const;

	// Intersect
	Point PositionAt() const;

protected:
	const Auto<const Intersect> m_intersect;
};