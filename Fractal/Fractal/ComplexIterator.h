#ifndef Fractal_ComplexIterator
#define Fractal_ComplexIterator

#include <complex>

#include "Iterator.h"

namespace Fractal { class Accumulator; }

namespace Fractal {
    class ComplexIterator : public Iterator<std::complex<double> >
    {
    public:
        ComplexIterator(Accumulator* accumulator00);
        ~ComplexIterator();
        
        const int IterateUntilEscaped(const int max_iter, const double escape);
    
    private:
        Accumulator* const m_accumulator00;
        
    };
}

#endif

