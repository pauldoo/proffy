#include "External.h"
#include "VectorSampler.h"

#include "Geometry.h"
#include "Utilities.h"

namespace Fractal
{
    template<typename T>
    VectorSampler<T>::VectorSampler(
        const Vector& top_left,
        const Vector& top_edge_vector,
        const Vector& left_edge_vector,
        GravityIterator<T>* iterator
    ) :
        m_top_left(top_left),
        m_top_edge_vector(top_edge_vector),
        m_left_edge_vector(left_edge_vector),
        m_iterator(iterator)
    {
    }
    
    template <typename T>
    VectorSampler<T>::~VectorSampler()
    {
    }
    
    template<typename T>
    void VectorSampler<T>::Render(Magick::Image& image, const double& exposure)
    {
        const int width = image.size().width();
        const int height = image.size().height();
        boost::progress_display progress( height );
        
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                const Vector seed =
                    m_top_left +
                    (x + 0.5) / width * m_top_edge_vector +
                    (y + 0.5) / height * m_left_edge_vector;
                m_iterator->Seed(seed);
                int count = m_iterator->IterateUntilTrapped();
                const double v = Utilities::Expose(count, exposure);
                image.pixelColor(x, y, Magick::ColorGray(v));
            }
            ++progress;
        }
    }
    
    template class VectorSampler< Fractal::Vector2 >;
}

