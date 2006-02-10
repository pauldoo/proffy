#ifndef Fractal_Accumulator
#define Fractal_Accumulator

#include <boost/multi_array.hpp>

#include "Geometry.h"

namespace Magick { class Image; }

namespace Fractal
{
    class Accumulator
    {
    public:
        Accumulator(const Magick::Image& image, const Matrix44& transform);
        
        void Accumulate(const Vector4& position, const double& value);
        
        void Render(Magick::Image& image, const double& exposure) const;
        
    private:
        const unsigned int m_width;
        const unsigned int m_height;
        const Matrix44 m_transform;
        
        boost::multi_array<double, 2> m_data;
    };
}

#endif

