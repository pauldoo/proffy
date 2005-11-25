#include "ComplexSampler.h"

#include <boost/progress.hpp>
#include <Magick++.h>

#include "ComplexIterator.h"

namespace Fractal
{
    ComplexSampler::ComplexSampler(
        const std::complex<double>& top_left,
        const std::complex<double>& bottom_right,
        ComplexIterator* iterator
    )
    :   m_top_left(top_left),
        m_bottom_right(bottom_right),
        m_iterator(iterator)
    {
    }
    
    void ComplexSampler::Render(Magick::Image& image, const double exposure)
    {
        const int width = image.size().width();
        const int height = image.size().height();
        const int max_iter = static_cast<int>(image.depth() * log(2) / exposure + 1);
        boost::progress_display progress( height );
        
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                const std::complex<double> z(
                    m_top_left.real() + (x + 0.5) / width * (m_bottom_right.real() - m_top_left.real()),
                    m_top_left.imag() + (y + 0.5) / height * (m_bottom_right.imag() - m_top_left.imag())
                );
                m_iterator->Seed(z);
                int count = m_iterator->IterateUntilEscaped(max_iter, 1e6);
                if (count == max_iter) {
                    count = 0;
                }
                const double v = 1.0 - std::exp(-count * exposure);
                image.pixelColor(x, y, Magick::ColorGray(v));
            }
            ++progress;
        }
    }

}

