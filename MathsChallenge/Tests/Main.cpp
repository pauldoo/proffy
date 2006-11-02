#include "Shared/Primes.h"
#include "Shared/PrimeFactors.h"
#include "Shared/ProperDivisors.h"

#include <iostream>

namespace {
	void TestPrimes(void)
	{
		std::cout << __FUNCTION__ << "\n";
		MC::Primes<unsigned int> primes;
		for (unsigned int i = 0; i <= 10; i++) {
			primes.Next();
			std::cout << primes.Current() << "\n";
		}
	}

	void TestPrimeFactors(void)
	{
		std::cout << __FUNCTION__ << "\n";
		for (unsigned int i = 0; i <= 10; i++) {
			std::cout << "Number: " << i << "\n";
			const std::vector<std::pair<unsigned int, unsigned int> > factors = MC::PrimeFactors(i);
			for (std::vector<std::pair<unsigned int, unsigned int> >::const_iterator j = factors.begin(); j != factors.end(); ++j) {
				std::cout << j->first << "^" << j->second << "\n";
			}
		}
	}

	void TestProperDivisors(void)
	{
		std::cout << __FUNCTION__ << "\n";
		for (unsigned int i = 0; i <= 10; i++) {
			std::cout << "Number: " << i << "\n";
			const std::vector<unsigned int> divisors = MC::ProperDivisors(i);
			for (std::vector<unsigned int>::const_iterator j = divisors.begin(); j != divisors.end(); ++j) {
				std::cout << *j << "\n";
			}
		}
		{
			std::cout << "Number: " << 220 << "\n";
			const std::vector<unsigned int> divisors = MC::ProperDivisors(220u);
			for (std::vector<unsigned int>::const_iterator j = divisors.begin(); j != divisors.end(); ++j) {
				std::cout << *j << "\n";
			}
		}
	}
}

int main(void)
{
	TestPrimes();
	TestPrimeFactors();
	TestProperDivisors();
	return 0;
}

