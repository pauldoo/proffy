#include "Shared/gcd.h"

template const unsigned int gcd<unsigned int>(const unsigned int, const unsigned int);

template<typename T> const T gcd(const T a, const T b)
{
    if (b == 0) {
        return a;
    } else {
        return gcd(b, a % b);
    }
}

