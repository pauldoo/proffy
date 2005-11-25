#ifndef Fractal_ComplexIterator
#define Fractal_ComplexIterator

#include <complex>
#include "Iterator.h"

namespace Fractal {
    class ComplexIterator : public Iterator<std::complex<double> >
    {
    public:
    
        virtual int IterateUntilEscaped(const int max_iter, const double escape);
    };
}

#endif

