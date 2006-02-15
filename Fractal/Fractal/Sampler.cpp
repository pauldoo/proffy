#include "External.h"
#include "Sampler.h"

#include "Geometry.h"

namespace Fractal
{
    template<typename T>
    Sampler<T>::Sampler()
    {
    }
    
    template<typename T>
    Sampler<T>::~Sampler()
    {
    }
    
    template class Sampler<std::complex<double> >;
    template class Sampler<Fractal::Vector2>;
}

