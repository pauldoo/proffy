/* http://mathschallenge.net/index.php?section=project&ref=problems&id=10 */
#include <iostream>
#include <vector>

namespace
{
    const bool isDivisible(const int p, const std::vector<int>& primes)
    {
        for (std::vector<int>::const_iterator i = primes.begin(); (i != primes.end()) && ((*i) * (*i) <= p); ++i) {
            if (p % (*i) == 0) {
                return true;
            }
        }
        return false;
    }
}

int main(void)
{
    std::vector<int> primes;
    long long tot = 0;
    for (int p = 2; p < 1000000; ++p)
    {
        if (!isDivisible(p, primes)) {
            primes.push_back(p);
            tot += p;
        }
    }
    std::cout << tot << std::endl;
    return 0;
}
