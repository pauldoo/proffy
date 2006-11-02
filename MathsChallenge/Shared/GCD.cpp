#include "Shared/GCD.h"

namespace MC {
    template const unsigned int GCD<unsigned int>(const unsigned int, const unsigned int);

    template<typename T> const T GCD(const T a, const T b)
    {
        if (b == 0) {
            return a;
        } else {
            return GCD(b, a % b);
        }
    }
}

