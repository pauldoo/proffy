#include "External.h"
#include "ComplexIterator.h"

#include "Accumulator.h"
#include "Geometry.h"

namespace Fractal
{
    ComplexIterator::ComplexIterator(Accumulator* accumulator00)
    :   m_accumulator00(accumulator00)
    {
    }
    
    ComplexIterator::~ComplexIterator()
    {
    }
    
    const int ComplexIterator::IterateUntilEscaped(const int max_iter, const double escape)
    {
        std::vector<std::complex<double> > points;
        int iter = 0;
        while (iter < max_iter && abs(Value()) <= escape) {
            if (m_accumulator00 != 0) {
                points.push_back(Value());
            }
            Iterate();
            iter++;
        }
        if (m_accumulator00 != 0 && iter != max_iter) {
            for (std::vector<std::complex<double> >::const_iterator i = points.begin(); i != points.end(); ++i) {
                m_accumulator00->Accumulate( Fractal::Geometry::ComplexToVector4(*i), 1.0 );
            }
        }
        return iter;
    }
}

