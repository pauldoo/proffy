#include "stdafx.h"
#include "JuliaSet.h"
#include "Scalar.h"
#include "Norms.h"
#include "ColorFuncs.h"

#include "TimingPool.h"

Color JuliaSet::ColorAt(Real i, Real j) const {
	TIMETHISFUNCTION;
	Scalar c(sin(3*i),sin(3*j));
	const int maxNoOfLoops = 1000; 
	for (int counter = 0 ; counter < maxNoOfLoops ; counter++) {
		if (Norm(c) > 4) return ColorFuncs::Hue(sqrt(Real(counter))/5+.5);
		c = c*c + Scalar(.1,-.65);
	}
	return Color();
}
