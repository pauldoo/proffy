#include "stdafx.h"
#include "SolidIntersect.h"
#include "ColorSolid.h"
#include "LineFuncs.h"

SolidIntersect::SolidIntersect(const Auto<const ColorSolid>& solid, const Line& lightRay, double distance)
:	m_solid(solid), m_distance(distance), m_point(LineFuncs::EvaluateAt(lightRay, distance))
{
}

Real SolidIntersect::DistanceAt() const {
	return m_distance;
}

Color SolidIntersect::Render(const Auto<const RenderInfo>& info, RenderMemory& renderMemory) const {
	return m_solid->Render(info, renderMemory);
}

// Intersect
Point SolidIntersect::NormalAt() const {
	return m_solid->NormalAt(m_point);
}

// Intersect
Point SolidIntersect::UVCoordsAt() const {
	return m_solid->UVCoordsAt(m_point);
}

// Intersect
Point SolidIntersect::PositionAt() const {
	return m_point;
}
