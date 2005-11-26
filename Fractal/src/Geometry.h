#ifndef Fractal_Geometry
#define Fractal_Geometry

#include <boost/numeric/ublas/matrix.hpp>
#include <boost/numeric/ublas/vector.hpp>
#include <complex>

namespace Fractal
{
    typedef boost::numeric::ublas::c_vector<double, 4> Vector4;
    typedef boost::numeric::ublas::c_matrix<double, 4, 4> Matrix44;
    
    namespace Geometry {
        Vector4 ComplexToVector(const std::complex<double>&);
        
        Matrix44 CreateBoundsTransform(
            const std::complex<double>& top_left,
            const std::complex<double>& bottom_right
        );
    }
}

#endif

