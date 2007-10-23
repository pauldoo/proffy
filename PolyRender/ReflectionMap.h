#pragma once
#include "TextureMap.h"

class ReflectionMap : public TextureMap {
public:
	ReflectionMap();

	Color ReflectionMap::ColorAt(const Real i, const Real j) const;

private:
	const Color m_color1;
	const Color m_color2;
};