#include "stdafx.h"
#include "LineFuncs.h"

#include "Line.h"
#include "Polynomial.h"
#include "TimingPool.h"

ParametricLine LineFuncs::ToParametricLine(const Line& line) {
	TIMETHISFUNCTION;
	const RPolynomial t = RPolynomial::MakeT();
	// TODO: Clean this shit up
	return ParametricLine(
		t * line.Direction().X() + line.Start().X(),
		t * line.Direction().Y() + line.Start().Y(),
		t * line.Direction().Z() + line.Start().Z() 
	);
}

Point LineFuncs::EvaluateAt(const Line& line, double t) {
	return line.Start() + line.Direction() * t;
}