#include "External.h"
#include "Iterator.h"

#include "Geometry.h"

namespace Fractal
{
    template<typename T>
    Iterator<T>::~Iterator()
    {
    }
    
    template class Iterator< std::complex<double> >;
    template class Iterator< Fractal::Vector2 >;
}

