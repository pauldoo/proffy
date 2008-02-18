#include <boost/random/mersenne_twister.hpp>
#include <iostream>

int main(void)
{
    boost::mt19937 generator;
    uint64_t sum = 0;
    for (int i = 0; i < 5000; i++) {
        const uint32_t v = generator();
        std::cout << v << ((i % 5 == 4) ? "\n" : " ");
        sum += v;
    }
    std::cout << "Sum: " << sum << "\n";
    return 0;
}

