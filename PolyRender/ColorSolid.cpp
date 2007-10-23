#include "stdafx.h"
#include "ColorSolid.h"
#include "Point.h"
#include "Exception.h"

Color ColorSolid::Render(const Auto<const RenderInfo>&, RenderMemory&) const {
	throw Exception("Not Implemented at this level");
}