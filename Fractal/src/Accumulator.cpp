#include "Accumulator.h"

#include <Magick++.h>
#include <cassert>

#include "Utilities.h"


namespace Fractal
{
    
    Accumulator::Accumulator(
        const Magick::Image& image,
        const Matrix44& transform
    )
    :   m_width(image.size().width()),
        m_height(image.size().height()),
        m_transform(transform),
        m_data(boost::extents[m_height][m_width])
    {
    }
    
    void Accumulator::Accumulate(const Vector4& position, const double& value)
    {
        const Vector4 transformed = prod(m_transform, position);
        if ( transformed[3] >= 0 && transformed[3] <= 1.0 ) {
            const int x = static_cast<int>(transformed[0] * m_width);
            const int y = static_cast<int>(transformed[1] * m_height);
            if (
                x >= 0 && x < static_cast<int>(m_width) &&
                y >= 0 && y < static_cast<int>(m_height)
            )
            {
                m_data[y][x] += value;
            }
        }
    }

    void Accumulator::Render(Magick::Image& image, const double& exposure) const
    {
        assert(image.size().width() == m_width);
        assert(image.size().height() == m_height);
        
        for (unsigned int y = 0; y < m_height; y++) {
            for (unsigned int x = 0; x < m_width; x++) {
                double v = Utilities::Expose(m_data[y][x], exposure);
                image.pixelColor( x, y, Magick::ColorGray(v) );
            }
        }
    }
    
}

