#ifndef Fractal_ComplexSampler
#define Fractal_ComplexSampler

#include <complex>

namespace Fractal { class ComplexIterator; }
namespace Magick { class Image; }

namespace Fractal
{
    class ComplexSampler
    {
    public:
        ComplexSampler(
            const std::complex<double>& top_left,
            const std::complex<double>& bottom_right,
            ComplexIterator* iterator
        );
        
        virtual void Render(Magick::Image& image, const double& exposure);
        
    private:
        const std::complex<double> m_top_left;
        const std::complex<double> m_bottom_right;
        ComplexIterator* const m_iterator;
    };
}

#endif

