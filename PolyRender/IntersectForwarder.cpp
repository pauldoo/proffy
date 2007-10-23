#include "stdafx.h"
#include "IntersectForwarder.h"
#include "Point.h"

IntersectForwarder::IntersectForwarder(const Auto<const Intersect>& intersect) 
:	m_intersect(intersect)
{
}

Real IntersectForwarder::DistanceAt() const {
	return m_intersect->DistanceAt();
}

Color IntersectForwarder::Render(const Auto<const RenderInfo>& info, RenderMemory& renderMemory) const {
	return m_intersect->Render(info, renderMemory);
}

Point IntersectForwarder::NormalAt() const {
	return m_intersect->NormalAt();
}

Point IntersectForwarder::UVCoordsAt() const {
	return m_intersect->UVCoordsAt();
}

Point IntersectForwarder::PositionAt() const {
	return m_intersect->PositionAt();
}
