#ifndef Fractal_VectorSampler
#define Fractal_VectorSampler

#include "GravityIterator.h"
#include "Sampler.h"

namespace Fractal
{
    template<typename T>
    class VectorSampler : public Sampler<T>
    {
    public:
        typedef T Vector;
        
        VectorSampler(
            const Vector& top_left,
            const Vector& top_edge_vector,
            const Vector& left_edge_vector,
            GravityIterator<Vector>* iterator
        );
        
        ~VectorSampler();
        
        // Fractal::Render
        void Render(Magick::Image& image, const double& exposure);

    private:
        const Vector m_top_left;
        const Vector m_top_edge_vector;
        const Vector m_left_edge_vector;
        GravityIterator<Vector>* const m_iterator;
    };
}

#endif

