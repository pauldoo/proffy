#include "stdafx.h"
#include "ColorTest.h"
#include "ColorFuncs.h"
#include "Exception.h"
#include "Vector3d.h"

std::string ColorTest::Name() const {
    return "Color Test";
}

bool IsColorValid(const COLORREF colorRef) {
	return (~0x00ffffff & colorRef) == 0;
}

Color RandomColor() {
	return Color(Norm(Random<Real>()), Norm(Random<Real>()), Norm(Random<Real>()));
}

bool IsSlopePositive(const double a, const int fa, const double b, const int fb) {
	if (a == b) throw Exception("points are equal: slope indeterminante");
	return (fa - fb)/(a - b) >= 0;
}

void ColorTest::Execute() {
	{
		const COLORREF red = ColorFuncs::ToCOLORREF(Color(std::numeric_limits<Real>::max(),0,0));
		const COLORREF green = ColorFuncs::ToCOLORREF(Color(0,std::numeric_limits<Real>::max(),0));
		const COLORREF blue = ColorFuncs::ToCOLORREF(Color(0,0,std::numeric_limits<Real>::max()));

		AssertEqual("Red", GetRValue(red), 255);
		AssertEqual("Red", GetGValue(red), 0);
		AssertEqual("Red", GetBValue(red), 0);

		AssertEqual("Green", GetRValue(green), 0);
		AssertEqual("Green", GetGValue(green), 255);
		AssertEqual("Green", GetBValue(green), 0);

		AssertEqual("Blue", GetRValue(blue), 0);
		AssertEqual("Blue", GetGValue(blue), 0);
		AssertEqual("Blue", GetBValue(blue), 255);
	}
	
	const int noOfTests = 200;
	for (int counter = 0 ; counter < noOfTests && TestPassing() ; counter++) {
		{
			Assert("ToCOLORREF produces valid colors", IsColorValid(ColorFuncs::ToCOLORREF(RandomColor())));
		}
		{
			const Real randomReal = Random<Real>();
			AssertNearlyEqual("Hue is periodic with period 1", ColorFuncs::Hue(randomReal), ColorFuncs::Hue(randomReal + 1));
		}
		{
			const Color color1 = RandomColor(); 
			const Color color2 = RandomColor(); 
			const COLORREF colorREF1 = ColorFuncs::ToCOLORREF(color1);
			const COLORREF colorREF2 = ColorFuncs::ToCOLORREF(color2);
			if (color1.X() != color2.X())
				Assert("Slope of Color ramp is positive", IsSlopePositive(color1.X(), GetRValue(colorREF1), color2.X(), GetRValue(colorREF2)));
			if (color1.Y() != color2.Y())
				Assert("Slope of Color ramp is positive", IsSlopePositive(color1.Y(), GetGValue(colorREF1), color2.Y(), GetGValue(colorREF2)));
			if (color1.Z() != color2.Z())
				Assert("Slope of Color ramp is positive", IsSlopePositive(color1.Z(), GetBValue(colorREF1), color2.Z(), GetBValue(colorREF2)));
			if (!TestPassing()) PersistentConsole::OutputString(ToString(color1) + "->" + ToString((int)GetRValue(colorREF1)) + " " + ToString((int)GetGValue(colorREF1)) + " " + ToString((int)GetBValue(colorREF1)) + "\n");
			if (!TestPassing()) PersistentConsole::OutputString(ToString(color2) + "->" + ToString((int)GetRValue(colorREF2)) + " " + ToString((int)GetGValue(colorREF2)) + " " + ToString((int)GetBValue(colorREF2)) + "\n");
		}
	}
	{
		AssertEqual("Negative Color Values go to Zero", ColorFuncs::ToCOLORREF(Color(-1, 0, 0)), COLORREF(0));
		AssertEqual("Negative Color Values go to Zero", ColorFuncs::ToCOLORREF(Color(0, -1, 0)), COLORREF(0));
		AssertEqual("Negative Color Values go to Zero", ColorFuncs::ToCOLORREF(Color(0, 0, -1)), COLORREF(0));
	}
	
} 