#pragma once
#include "Point.h"

class Line {
public:
	Line(const Point& start, const Point& direction);
	Point Start() const;
	Point Direction() const;
private:
	const Point m_start, m_direction;
};