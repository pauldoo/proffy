#include "stdafx.h"
#include "Norms.h"
#include "Scalar.h"

Real NormSquared(const Scalar &X) {
	return norm(X);
}

Real NormSquared(const Real X) {
	return X * X;
}

Real Norm(const Scalar &X) {
	return abs(X);
}

Real Norm(const Real X) {
	return fabs(X);
}

template <typename Type>
Real NormSquared(const std::vector<Type> &X) {
	if (X.empty()) return 0;
	double result = 0;
	for (int i = 0 ; i < (int)X.size() ; i++) 	{
		result += NormSquared(X[i]);
	}
	return result/X.size();
}

template Real NormSquared(const std::vector<Scalar>&);
template Real NormSquared(const std::vector<Real>&);
template Real Norm(const std::vector<Scalar>&);
template Real Norm(const std::vector<Real>&);
