#ifndef Fractal_ComplexSampler
#define Fractal_ComplexSampler

#include "Sampler.h"

#include <complex>

namespace Fractal { class ComplexIterator; }
namespace Magick { class Image; }

namespace Fractal
{
    class ComplexSampler : Sampler<std::complex<double> >
    {
    public:
        ComplexSampler(
            const std::complex<double>& top_left,
            const std::complex<double>& bottom_right,
            ComplexIterator* iterator
        );
        
        // Fractal::Sampler
        void Render(Magick::Image& image, const double& exposure);
        
    private:
        const std::complex<double> m_top_left;
        const std::complex<double> m_bottom_right;
        ComplexIterator* const m_iterator;
    };
}

#endif

