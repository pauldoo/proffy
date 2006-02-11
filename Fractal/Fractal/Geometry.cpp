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
        
        
        const Vector2 ComplexToVector2(const std::complex<double>& value)
        {
            Vector2 result;
            result(0) = value.real();
            result(1) = value.imag();
            return result;
        }

        const Vector4 ComplexToVector4(const std::complex<double>& value)
        {
            return Vector2ToVector4(ComplexToVector2(value));
        }

        const Vector4 Vector2ToVector4(const Vector2& value)
        {
            Vector4 result;
            result(0) = value(0);
            result(1) = value(1);
            result(2) = 0.0;
            result(3) = 1.0;
            return result;
        }
        
        const Matrix44 CreateBoundsTransform(
            const Vector2& top_left,
            const Vector2& bottom_right
        )
        {
            Matrix44 result;
            result.clear();
            result(0, 0) = 1.0 / (bottom_right(0) - top_left(0));
            result(0, 3) = -top_left(0) * result(0, 0);
            result(1, 1) = 1.0 / (bottom_right(1) - top_left(1));
            result(1, 3) = -top_left(1) * result(1, 1);
            result(2, 3) = 0.5;
            result(3, 3) = 1;
            return result;
        }
        
        const Matrix44 CreateBoundsTransform(
            const std::complex<double>& top_left,
            const std::complex<double>& bottom_right
        )
        {
            return CreateBoundsTransform(
                ComplexToVector2(top_left),
                ComplexToVector2(bottom_right)
            );
        }
    }
}

