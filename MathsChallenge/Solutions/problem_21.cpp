// http://mathschallenge.net/index.php?section=project&ref=problems&id=21

#include "Shared/ProperDivisors.h"

#include <iostream>
#include <numeric>
#include <set>

namespace {
	const unsigned int SumOfProperDivisors(const unsigned int value)
	{
		const std::vector<unsigned int> divisors = MC::ProperDivisors(value);
		return std::accumulate(divisors.begin(), divisors.end(), 0);
	}
}

int main(void)
{
	std::set<unsigned int> consideredMembers;
	for (unsigned int i = 0; i < 10000; i++) {
		const unsigned int j = SumOfProperDivisors(i);
		if (i != j && j < 10000 && SumOfProperDivisors(j) == i) {
			consideredMembers.insert(i);
			consideredMembers.insert(j);
		}
	}

	std::cout << std::accumulate(consideredMembers.begin(), consideredMembers.end(), 0) << "\n";

	return 0;
}
