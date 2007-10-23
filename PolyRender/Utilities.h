#pragma once
#include <sstream>

#include "Precision.h"
#include "ScalarDeclarations.h"

template <typename Type>
Type Random();

template<typename Type>
std::string ToString(Type val) {
	std::stringstream strm;
	strm << val;
	return strm.str();
}


// TODO: hmmm ... this could be used to print a vector. Maybe 
// vector3d should implement this type or a common type.
template<typename Type>
std::string ToString(std::vector<Type> valArray) {
	std::string result;
	result += "[";
	if (!valArray.empty()) {
		typename std::vector<Type>::const_iterator val_iter = valArray.begin();
		result += ToString(*val_iter);
		val_iter++;
		for ( ; val_iter != valArray.end() ; val_iter++) {
			result += ", " + ToString(*val_iter);
		}
	}
	return result + "]";
}

std::string ToString(const Scalar &val);

// TODO: Not been tested.
template <typename> class Maybe;
const Maybe<Real> FindSmallestPositiveRootOfQuadratic(const Real a, const Real b, const Real c);

void CheckIfNull(const void*);

