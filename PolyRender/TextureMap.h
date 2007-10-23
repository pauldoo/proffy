#pragma once
#include "Vector3d.h"
#include "ColorDeclarations.h"
#include "LinkCount.h"

class TextureMap : public LinkCount {
public: 
	virtual Color ColorAt(const Real i, const Real j) const = 0;
};
