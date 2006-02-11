#ifndef Fractal_JuliaIterator
#define Fractal_JuliaIterator

#include "ComplexIterator.h"

namespace Fractal { class Accumulator; }

namespace Fractal {
    class JuliaIterator : public ComplexIterator
    {
    public:
        JuliaIterator(Accumulator* accumulator00, const Type& param);
    
        // Fractal::Iterator
        void Seed(const Type& seed);
        
        // Fractal::Iterator
        const Type& Value(void) const;
    
        // Fractal::Iterator
        const Type& Iterate(void);
        
    private:
        const std::complex<double> m_c;
        std::complex<double> m_z;
    };
}

#endif

