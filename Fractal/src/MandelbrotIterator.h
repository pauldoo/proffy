#ifndef Fractal_MandelbrotIterator
#define Fractal_MandelbrotIterator

#include "ComplexIterator.h"

namespace Fractal {
    class MandelbrotIterator : public ComplexIterator<double>
    {
    public:
        // Iterator::Seed
        void Seed(const Type& seed);
        
        // Iterator::Value
        const Type& Value(void) const;
        
        // Iterator::Iterator
        const Type& Iterate(void);
        
    private:
        std::complex<double> m_z0;
        std::complex<double> m_z;
    };
}

#endif

