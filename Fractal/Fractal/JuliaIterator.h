#ifndef Fractal_JuliaIterator
#define Fractal_JuliaIterator

#include "ComplexIterator.h"

namespace Fractal { class Accumulator; }

namespace Fractal {
    class JuliaIterator : public ComplexIterator
    {
    public:
        JuliaIterator(Accumulator* accumulator00, const Type& param);
    
        // Iterator::Seed
        void Seed(const Type& seed);
        
        // Iterator::Value
        const Type& Value(void) const;
    
        // Iterator::Iterate
        const Type& Iterate(void);
        
    private:
        const std::complex<double> m_c;
        std::complex<double> m_z;
    };
}

#endif

