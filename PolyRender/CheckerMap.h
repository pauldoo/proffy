#pragma once
#include "TextureMap.h"

class CheckerMap : public TextureMap {
public:
	CheckerMap(
		const Color& color1 = Color(0,0,0), 
		const Color& color2 = Color(3,3,3)
	);

	Color ColorAt(const Real i, const Real j) const;

private:
	const Color m_color1;
	const Color m_color2;
};