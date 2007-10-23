#pragma once
#include "TextureMap.h"

class ColorMap : public TextureMap {
public:
	ColorMap(const Color& color = Color());
	Color ColorAt(const Real i, const Real j) const;

private:
	const Color m_color;
};
