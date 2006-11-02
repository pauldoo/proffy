#include "Shared/PrimeFactors.h"

#include "Shared/Primes.h"

namespace MC {
	template const std::vector<std::pair<unsigned int, unsigned int> > PrimeFactors<unsigned int>(const unsigned int& val);

	template<typename T> const std::vector<std::pair<T, unsigned int> > PrimeFactors(const T& value)
	{
		Primes<T> primes;
		std::vector<std::pair<T, T> > result;
		T remainder = value;
		while (remainder > 1) {
			primes.Next();
			unsigned int count = 0;
			while (remainder % primes.Current() == 0) {
				remainder /= primes.Current();
				count++;
			}
			if (count > 0) {
				result.push_back(std::make_pair(primes.Current(), count));
			}
		}
		return result;
	}
}
