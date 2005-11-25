#ifndef Fractal_JuliaIterator
#define Fractal_JuliaIterator

#include "ComplexIterator.h"

namespace Fractal {
    class JuliaIterator : public ComplexIterator
    {
    public:
        JuliaIterator(const Type& param);
    
        // Iterator::Seed
        void Seed(const Type& seed);
        
        // Iterator::Seed
        const Type& Value(void) const;
    
        // Iterator::Seed
        const Type& Iterate(void);
        
    private:
        const std::complex<double> m_c;
        std::complex<double> m_z;
    };
}

#endif

