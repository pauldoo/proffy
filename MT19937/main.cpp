#include <boost/random/mersenne_twister.hpp>
#include <iostream>

int main(void)
{
    boost::mt19937 generator;
    for (int i = 0; i < 5000; i++) {
        std::cout << generator() << ((i % 5 == 4) ? "\n" : " ");
    }
    return 0;
}

