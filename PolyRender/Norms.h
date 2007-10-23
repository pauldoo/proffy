#pragma once
#include "ScalarDeclarations.h"
#include "Precision.h"

template <typename Type>
Real NormSquared(const std::vector<Type>& X);


template <typename T, typename K> class Vector3d;
template <typename T, typename K> Real NormSquared(const Vector3d<T, K> &V);

Real NormSquared(const Scalar &X);
Real NormSquared(const Real X);

Real Norm(const Scalar &X);
Real Norm(const Real X);

const Real maxAcceptableErrorSquared = 1e-3;
template <typename Type>
bool NearlyZeroQ(Type X) {
    return ::NormSquared(X) < maxAcceptableErrorSquared * maxAcceptableErrorSquared;
}

template <typename Type>
Real Norm(const Type &X) {
	return sqrt(NormSquared(X));
}
