#include "stdafx.h"
#include "RenderInfo.h"

RenderInfo::RenderInfo(
	const Auto<const Intersect>& intersect,
	const Auto<const World>& world,
	const Line& line)
 :	m_intersect(intersect), 
	m_world(world),
	m_line(line)
{
}
