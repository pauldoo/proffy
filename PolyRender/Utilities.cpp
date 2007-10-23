#include "stdafx.h"
#include "Utilities.h"
#include "Maybe.h"
#include "Exception.h"
#include "Scalar.h"
#include "Norms.h"

template<>
Real Random<Real>() {
	return (2.0*std::rand())/RAND_MAX - 1;
}

template<>
Scalar Random<Scalar>() {
	return Scalar(Random<Real>(),Random<Real>());
}

std::string ToString(const Scalar &val) {
	std::string result;
	if (val == 0.) return "0";
	if (val.real() != 0 && val.imag() != 0) result += "(";
    if (val.real() != 0) result += ToString(val.real());
	if (val.imag() > 0 && val.real() != 0) result += "+";
	if (val.imag() < 0) result += "-";
	if (val.imag() != 0 && Norm(val.imag()) != 1) result += ToString(Norm(val.imag()));
	if (val.imag() != 0) result += "i";
	if (val.real() != 0 && val.imag() != 0) result += ")";
	return result;
}

const Maybe<Real> FindSmallestPositiveRootOfQuadratic(const Real a, const Real b, const Real c) {
	const Real temp = b*b - 4*a*c;
	if (temp > 0) {
		Real root;
		if ((root = (-b-sqrt(temp))/(2*a)) > 0) {
			return root;
		} else if ((root = (-b+sqrt(temp))/(2*a)) > 0) {
			return root;
		}
	}
	return Maybe<Real>();
}
