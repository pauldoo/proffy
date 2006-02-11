#include <Magick++.h>

#include "Fractal/Accumulator.h"
#include "Fractal/Geometry.h"
#include "Fractal/GravityIterator.h"
#include "Fractal/Utilities.h"
#include "Fractal/VectorSampler.h"

namespace
{
    const int width = 1024;
    const int height = 1024;
    const int supersample = 2;
    const int accum_scale = 1;
    const double damping = 5e-2;
    const double da = 1e-1;
    const double distance_cutoff = 1e-2;
    const double image_exposure = 1;
    const double accumulator_exposure = 1;

    void ProduceGravityFractal(const std::vector<Fractal::Vector2>& source_list, Magick::Image& image, Magick::Image& accum)
    {
        Fractal::Vector2 top_left;
        top_left(0) = 0.0;
        top_left(1) = 0.0;
        Fractal::Vector2 top_edge_vector;
        top_edge_vector(0) = 1.0;
        top_edge_vector(1) = 0.0;
        Fractal::Vector2 left_edge_vector;
        left_edge_vector(0) = 0.0;
        left_edge_vector(1) = 1.0;
        Fractal::Vector2 bottom_right = top_left + top_edge_vector + left_edge_vector;;
        
        const Fractal::Matrix44 transform = Fractal::Geometry::CreateBoundsTransform(top_left, bottom_right);
        Fractal::Accumulator accumulator(accum, transform);
        Fractal::GravityIterator< Fractal::Vector2 > iterator(&accumulator, source_list, da, damping, distance_cutoff);
        Fractal::VectorSampler< Fractal::Vector2 > sampler(top_left, top_edge_vector, left_edge_vector, &iterator);
        sampler.Render(image, image_exposure);
        accumulator.Render(accum, accumulator_exposure);
    }
    
    const Fractal::Vector2 RandomVector(void)
    {
        Fractal::Vector2 result;
        result(0) = Fractal::Utilities::Random();
        result(1) = Fractal::Utilities::Random();
        return result;
    }
}

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
    
    std::vector<Fractal::Vector2> masses;
    //for (int i = 0; i < sources; i++) {
    //    masses.push_back( RandomVector() );
    //}
    {
        Fractal::Vector2 pos;
        pos(0) = 0.5;
        pos(1) = 0.5;
        masses.push_back(pos);
    }
    {
        Fractal::Vector2 pos;
        pos(0) = 0.6;
        pos(1) = 0.5;
        masses.push_back(pos);
    }
    {
        Fractal::Vector2 pos;
        pos(0) = 0.5;
        pos(1) = 0.6;
        masses.push_back(pos);
    }
    
    Magick::Image image(Magick::Geometry(width * supersample, height * supersample), Magick::ColorRGB(1.0, 0.0, 0.0));
    Magick::Image accum(Magick::Geometry(width * supersample * accum_scale, height * supersample * accum_scale), Magick::ColorRGB(1.0, 0.0, 0.0));
    
    ProduceGravityFractal(masses, image, accum);

    image.scale(Magick::Geometry(width, height));
    accum.scale(Magick::Geometry(width * accum_scale, height * accum_scale));
    image.write("images/gravity_image.png");
    accum.write("images/gravity_accum.png");
    
    return 0;
}

