#include "Sampler.h"

#include "Geometry.h"

#include <complex>

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

