#ifndef Fractal_Geometry
#define Fractal_Geometry

namespace Fractal
{
    typedef boost::numeric::ublas::c_vector<double, 2> Vector2;
    typedef boost::numeric::ublas::c_vector<double, 4> Vector4;
    typedef boost::numeric::ublas::c_matrix<double, 4, 4> Matrix44;

    typedef boost::numeric::ublas::c_vector<int, 2> iVector2;
    
    namespace Geometry {
        const Vector2 ComplexToVector2(const std::complex<double>&);
        const Vector4 ComplexToVector4(const std::complex<double>&);
        const Vector4 Vector2ToVector4(const Vector2&);
        
        const Matrix44 CreateBoundsTransform(
            const Vector2& top_left,
            const Vector2& bottom_right
        );
        
        const Matrix44 CreateBoundsTransform(
            const std::complex<double>& top_left,
            const std::complex<double>& bottom_right
        );
    }
}

#endif

