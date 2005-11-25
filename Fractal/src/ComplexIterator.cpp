#include "ComplexIterator.h"

namespace Fractal
{
    int ComplexIterator::IterateUntilEscaped(const int max_iter, const double escape)
    {
        int iter = 0;
        while (iter < max_iter && abs(Value()) <= escape) {
            Iterate();
            iter++;
        }
        return iter;
    }
}

