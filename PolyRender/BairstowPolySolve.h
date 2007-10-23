#pragma once

#include "RPolynomialDeclarations.h"
#include "Precision.h"
#include "MaybeDeclarations.h"
#include "NonCreatable.h"

class BairstowPolySolve : public NonCreatable {
public:
	static Maybe<Real> FindSmallestPositiveRoot(const RPolynomial&);
	static Maybe<Real> FindSmallestPositiveRoot(const RPolynomial&, std::vector<std::pair<Real, Real> >& initial);
};
