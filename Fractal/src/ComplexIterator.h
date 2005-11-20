#ifndef Fractal_ComplexIterator
#define Fractal_ComplexIterator

#include <complex>
#include "Iterator.h"

namespace Fractal {
    template <typename T>
    class ComplexIterator : public Iterator<std::complex<T> >
    {
    public:
    
        virtual int IterateUntilEscaped(const int max_iter, const T escape)
        {
            int iter = 0;
            while (iter < max_iter && abs(Value()) <= escape) {
                Iterate();
                iter++;
            }
            return iter;
        }
    };
}

#endif

