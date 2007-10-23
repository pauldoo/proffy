#include "stdafx.h"
#include "ReflectionMap.h"
#include "Utilities.h"

namespace {
	int RoundDown(const double x) {
		if (x < 0) return static_cast<int>(INT_MAX + x) - INT_MAX;
		else return static_cast<int>(x);
	}
}

ReflectionMap::ReflectionMap(
	const Color& color1,
	const Color& color2
) : m_color1(color1), m_color2(color2) {}

Color ReflectionMap::ColorAt(Real i, Real j) const {
	TIMETHISFUNCTION;
	if (RoundDown(5 * i) + RoundDown(5 * j) & 1 ) return m_color1;
	else return m_color2;
}