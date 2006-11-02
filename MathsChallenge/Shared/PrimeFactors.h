#ifndef MC_PRIMEFACTORS_H
#define MC_PRIMEFACTORS_H

#include <vector>

namespace MC {
	template<typename T> const std::vector<std::pair<T, unsigned int> > PrimeFactors(const T&);
}

#endif

