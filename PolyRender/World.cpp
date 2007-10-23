#include "stdafx.h"
#include "World.h"
#include "Solid.h"

World::World(
	const Auto<const Solid>& solid, 
	const ProjectionPlane& projectionPlane,
	const LightList& lights)
:	m_solid(solid), 
	m_projectionPlane(projectionPlane),
	m_lights(lights)
{
}

Auto<const Solid> World::GetSolid() const {
	return m_solid;
}

ProjectionPlane World::GetProjectionPlane() const {
	return m_projectionPlane;
}

const World::LightList& World::GetLights() const {
	return m_lights;
}
