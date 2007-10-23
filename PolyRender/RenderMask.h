#pragma once
#include "LinkCount.h"
#include "ColorDeclarations.h"

template <typename> class Auto;
class RenderInfo;
class RenderMemory;

class RenderMask : public LinkCount {	
public:
	virtual Color Render(const Auto<const RenderInfo>&, RenderMemory&) const = 0;
};