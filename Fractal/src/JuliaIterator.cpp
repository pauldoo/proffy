#include "JuliaIterator.h"

namespace Fractal {
    JuliaIterator::JuliaIterator(
        Accumulator* accumulator00,
        const JuliaIterator::Type& param
    )
    :   ComplexIterator(accumulator00),
        m_c(param)
    {
    };

    void JuliaIterator::Seed(const JuliaIterator::Type& seed)
    {
        m_z = seed;
    }
    
    const JuliaIterator::Type& JuliaIterator::Value(void) const
    {
        return m_z;
    }

    const JuliaIterator::Type& JuliaIterator::Iterate(void)
    {
        m_z = m_z * m_z + m_c;
        //m_z = sin(m_z) * m_c;
        return m_z;
    }
}

