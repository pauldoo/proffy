#include "stdafx.h"
#include "Line.h"

Line::Line(const Point& start, const Point& direction) : m_start(start), m_direction(direction) 
{
}

Point Line::Start() const {
	return m_start;
}

Point Line::Direction() const {
	return m_direction;
}
