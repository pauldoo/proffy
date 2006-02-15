#include "External.h"
#include "MandelbrotIterator.h"

namespace Fractal {
    MandelbrotIterator::MandelbrotIterator(Accumulator* accumulator00)
    :   ComplexIterator(accumulator00)
    {
    }
    
    void MandelbrotIterator::Seed(const MandelbrotIterator::Type& seed)
    {
        m_z0 = seed;
        m_z = seed;
    }
    
    const MandelbrotIterator::Type& MandelbrotIterator::Value(void) const
    {
        return m_z;
    }
    
    const MandelbrotIterator::Type& MandelbrotIterator::Iterate(void)
    {
        m_z = m_z * m_z + m_z0;
        return m_z;
    }
}

