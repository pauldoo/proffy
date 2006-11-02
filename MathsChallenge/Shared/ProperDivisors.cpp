#include "Shared/ProperDivisors.h"

#include "Shared/PrimeFactors.h"

namespace MC {
	template const std::vector<unsigned int> ProperDivisors(const unsigned int&);

	template<typename T> const std::vector<T> ProperDivisors(const T& value)
	{
		const std::vector<std::pair<T, unsigned int> > primeFactors = PrimeFactors(value);
		std::vector<T> result;
		if (primeFactors.empty()) {
			// There are no primes that divide in (ie value == 1)
			return result;
		} else {
			result.push_back(1);
			for (std::vector<std::pair<T, unsigned int> >::const_iterator i = primeFactors.begin(); i != primeFactors.end(); ++i) {
				const std::pair<T, unsigned int>& pf = *i;
				T primeToThePower = 1;
				const unsigned int currentSize = result.size();
				for (unsigned int power = 1; power <= pf.second; power++) {
					primeToThePower *= pf.first;
					for (unsigned int j = 0; j < currentSize; j++) {
						const T divisor = result[j] * primeToThePower;
						if (divisor != value) {
							result.push_back(divisor);
						}
					}
				}
			}
		}
		return result;
	}
}
