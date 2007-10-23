#pragma once
#include "ProjectionPlane.h"
#include "Auto.h"

class Solid;
class Light;

class World : public LinkCount {
public:	
	typedef std::vector<Auto<const Light> > LightList;
	
	World(
		const Auto<const Solid>& m_solid, 
		const ProjectionPlane& m_projectionPlane, 
		const LightList& light);
	
	Auto<const Solid> GetSolid() const;
	ProjectionPlane GetProjectionPlane () const;
	const LightList& GetLights() const;
	
private:
	const ProjectionPlane m_projectionPlane;
	const Auto<const Solid> m_solid;
	const LightList m_lights;
};
