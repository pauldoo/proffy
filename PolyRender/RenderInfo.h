#pragma once

#include "Line.h"
#include "Auto.h"

class Intersect;
class World;

class RenderInfo : public LinkCount {
public:
	RenderInfo(
		const Auto<const Intersect>& intersect,
		const Auto<const World>& world,
		const Line& line);

	//TODO: make private
	const Auto<const Intersect> m_intersect;
	const Auto<const World> m_world;
	const Line m_line;
};
