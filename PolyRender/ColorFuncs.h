#pragma once
#include "ColorDeclarations.h"
#include "NonCreatable.h"

class ColorFuncs : public NonCreatable {
public:
	// Converts the RGB Intensity vector to it's corresponding RGB value
	static COLORREF ToCOLORREF(const Color& color);

	// function takes a specific hue and returns a color;
	static Color Hue(Real hue);
};
