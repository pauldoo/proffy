#include "stdafx.h"
#include "ColorMap.h"

ColorMap::ColorMap(const Color& color) : m_color(color) {}

Color ColorMap::ColorAt(Real, Real) const {
	return m_color;
}