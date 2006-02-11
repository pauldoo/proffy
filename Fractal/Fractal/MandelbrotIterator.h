#ifndef Fractal_MandelbrotIterator
#define Fractal_MandelbrotIterator

#include "ComplexIterator.h"

namespace Fractal { class Accumulator; }

namespace Fractal {
    class MandelbrotIterator : public ComplexIterator
    {
    public:
        MandelbrotIterator(Accumulator* accumulator00);
        
        // Fractal::Iterator
        void Seed(const Type& seed);
        
        // Fractal::Iterator
        const Type& Value(void) const;
        
        // Fractal::Iterator
        const Type& Iterate(void);
        
    private:
        std::complex<double> m_z0;
        std::complex<double> m_z;
    };
}

#endif

