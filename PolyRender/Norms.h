#pragma once
#include "ScalarDeclarations.h"
#include "Precision.h"

const Real maxAcceptableErrorSquared = 1e-3;
template <typename Type>
bool NearlyZeroQ(Type X) {
	return NormSquared(X) < maxAcceptableErrorSquared * maxAcceptableErrorSquared;
}

template <typename Type>
Real NormSquared(const std::vector<Type>& X);

Real NormSquared(const Scalar &X);
Real NormSquared(const Real X);

Real Norm(const Scalar &X);
Real Norm(const Real X);

template <typename Type>
Real Norm(const Type &X) {
	return sqrt(NormSquared(X));
}