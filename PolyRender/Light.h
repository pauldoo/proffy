#pragma once

#include "Point.h"
#include "ColorDeclarations.h"
#include "LinkCount.h"

class Light : public LinkCount {
public:
	Light(const Point& position, const Color& color);
	const Color m_color;
	const Point m_position;
};
