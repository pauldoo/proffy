#include "stdafx.h"
#include "ColorFuncs.h"
#include "Point.h"

namespace {
	Real ColorValue(Real x) {
		x = 3*(Real)fabs(fmod(fabs(x),1)-.5);
		return (Real)(fabs(x-1)-fabs(x-.5)+.5);
	}
}

// function takes a specific hue and returns it's RGB value
Color ColorFuncs::Hue(const Real hue) {
	return Color( 
		ColorValue(hue + 0 / 3.f), 
		ColorValue(hue + 1 / 3.f), 
		ColorValue(hue + 2 / 3.f)
	);
}

COLORREF ColorFuncs::ToCOLORREF(const Color& color) {
	if (color.X() < 0 || color.Y() < 0 || color.Z() < 0 ) return RGB(0,0,0);
    return RGB(
        255*(1 - exp(-color.X())), 
        255*(1 - exp(-color.Y())), 
        255*(1 - exp(-color.Z()))
    );
}