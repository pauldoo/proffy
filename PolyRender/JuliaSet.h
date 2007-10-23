#pragma once
#include "TextureMap.h"

class JuliaSet : public TextureMap {
public:
	Color ColorAt(const Real i, const Real j) const;
};