#pragma once

#include "CPolynomialDeclarations.h"
#include "RPolynomialDeclarations.h"
#include "Precision.h"
#include "MaybeDeclarations.h"
#include "ScalarDeclarations.h"
#include "NonCreatable.h"

// Returns the roots of coefficient using the iterative newton-raphson method.
// WARNING Potential divide-by-zero, periodic or chaotic behavior
class NewtonPolySolve : public NonCreatable {
public:
	static Maybe<Real> FindSmallestPositiveRoot(const RPolynomial&, const Real lastSmallestPositve = 0);
	
	// TODO: Fix this reference parameter
	static ScalarList FindRoots(const RPolynomial&, const ScalarList& guessList = ScalarList());
	static ScalarList FindRoots(const CPolynomial&, const ScalarList& guessList = ScalarList());
};
