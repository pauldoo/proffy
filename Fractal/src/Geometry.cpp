#include "Geometry.h"

namespace Fractal
{
    namespace Geometry
    {
        
        /* Vector 4 structure:
            { 0 }
            { 1 }
            { 2 }
            { 3 }
        */
        
        /* Matrix 44 structure:
            { 0, 1, 2, 3 }
            { 4, 5, 6, 7 }
            { 8, 9, 10, 11 }
            { 12, 13, 14, 15 }
        */
        
        
        Vector4 ComplexToVector(const std::complex<double>& value)
        {
            Vector4 result;
            result(0) = value.real();
            result(1) = value.imag();
            result(2) = 0.0;
            result(3) = 1.0;
            return result;
        }
        
        Matrix44 CreateBoundsTransform(
            const std::complex<double>& top_left,
            const std::complex<double>& bottom_right
        )
        {
            Matrix44 result;
            result.clear();
            result(0, 0) = 1.0 / (bottom_right.real() - top_left.real());
            result(0, 3) = -top_left.real() * result(0, 0);
            result(1, 1) = 1.0 / (bottom_right.imag() - top_left.imag());
            result(1, 3) = -top_left.imag() * result(1, 1);
            result(2, 3) = 0.5;
            result(3, 3) = 1;
            return result;
        }
        
    }
}

