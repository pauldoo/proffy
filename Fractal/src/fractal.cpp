#include <Magick++.h>
#include <boost/program_options/options_description.hpp>
#include <boost/program_options/parsers.hpp>
#include <boost/program_options/variables_map.hpp>
#include <boost/progress.hpp>
#include <cmath>
#include <complex>
#include <exception>
#include <iostream>
#include <sstream>
#include <string>

#include "Accumulator.h"
#include "ComplexSampler.h"
#include "Geometry.h"
#include "JuliaIterator.h"
#include "MandelbrotIterator.h"


void ProduceJuliaRendering(
    Magick::Image& image,
    Magick::Image& accum,
    const std::complex<double>& top_left,
    const std::complex<double>& bottom_right,
    const std::complex<double>& c,
    const double& exposure,
    const double& accum_exposure
)
{
    const Fractal::Matrix44 transform = Fractal::Geometry::CreateBoundsTransform(top_left, bottom_right);
    Fractal::Accumulator accumulator(accum, transform);
    Fractal::JuliaIterator iterator(&accumulator, c);
    Fractal::ComplexSampler sampler(top_left, bottom_right, &iterator);
    sampler.Render(image, exposure);
    accumulator.Render(accum, accum_exposure);
    //ProduceRendering( image, top_left, bottom_right, iterator, exposure );
}

void ProduceMandelbrotRendering(
    Magick::Image& image,
    Magick::Image& accum,
    const std::complex<double>& top_left,
    const std::complex<double>& bottom_right,
    const double& exposure,
    const double& accum_exposure
)
{
    const Fractal::Matrix44 transform = Fractal::Geometry::CreateBoundsTransform(top_left, bottom_right);
    Fractal::Accumulator accumulator(accum, transform);
    Fractal::MandelbrotIterator iterator(&accumulator);
    Fractal::ComplexSampler sampler(top_left, bottom_right, &iterator);
    sampler.Render(image, exposure);
    accumulator.Render(accum, accum_exposure);
    //ProduceRendering( image, top_left, bottom_right, iterator, exposure );
}

int main(int argc, char* argv[])
{
    try {
        std::ios::sync_with_stdio(false);
        int width, height, supersample;
        double escape, exposure, accum_exposure, left, right, top, bottom;
        std::string output, accum_output;
        
        boost::program_options::options_description general_options("Image options");
        general_options.add_options()
            ("help", "produce help message")
            ("width", boost::program_options::value<int>(&width)->default_value(640), "width of output image in pixels")
            ("height", boost::program_options::value<int>(&height)->default_value(480), "height of output image in pixels")
            ("exposure", boost::program_options::value<double>(&exposure)->default_value(0.05), "exposure sensitivity")
            ("accum_exposure", boost::program_options::value<double>(&accum_exposure)->default_value(0.05), "accumulated orbits exposure sensitivity")
            ("output", boost::program_options::value<std::string>(&output)->default_value("output.pnm"), "output image file")
            ("accum_output", boost::program_options::value<std::string>(&accum_output)->default_value("output_accum.pnm"), "accumulated orbits output image file")
            ("supersample", boost::program_options::value<int>(&supersample)->default_value(2), "supersample in each dimension")
        ;
    
        boost::program_options::options_description fractal_options("Fractal options");
        fractal_options.add_options()
            ("escape", boost::program_options::value<double>(&escape)->default_value(1000000), "height of output image in pixels")
            ("left", boost::program_options::value<double>(&left)->default_value(-2.0), "real value at left of image")
            ("top", boost::program_options::value<double>(&top)->default_value(-1.5), "imaginary value at top of image")
            ("right", boost::program_options::value<double>(&right)->default_value(2.0), "real value at right of image")
            ("bottom", boost::program_options::value<double>(&bottom)->default_value(1.5), "imaginary value at bottom of image")
            ("julia_real", boost::program_options::value<double>(), "julia constant (real)")
            ("julia_imag", boost::program_options::value<double>(), "julia constant (imaginary)")
        ;
        
        boost::program_options::options_description all_options("Command line options");
        all_options.add(general_options).add(fractal_options);
        
        boost::program_options::variables_map variables;
        boost::program_options::store(boost::program_options::parse_command_line(argc, argv, all_options), variables);
        boost::program_options::notify(variables);
        
        if (variables.count("help")) {
            std::cout << all_options << std::endl;
            return 1;
        }
        
        const std::complex<double> top_left(left, top);
        const std::complex<double> bottom_right(right, bottom);
        
        Magick::Image image(Magick::Geometry(width * supersample, height * supersample), Magick::ColorRGB(1.0, 0.0, 0.0));
        Magick::Image accum(Magick::Geometry(width * supersample, height * supersample), Magick::ColorRGB(1.0, 0.0, 0.0));
        
        if (variables.count("julia_real")) {
            const std::complex<double> c(
                variables["julia_real"].as<double>(),
                variables["julia_imag"].as<double>()
            );
            ProduceJuliaRendering(image, accum, top_left, bottom_right, c, exposure, accum_exposure);
        } else {
            ProduceMandelbrotRendering(image, accum, top_left, bottom_right, exposure, accum_exposure);
        }
        image.scale(Magick::Geometry(width, height));
        accum.scale(Magick::Geometry(width, height));
        image.write(output);
        accum.write(accum_output);
        
    } catch (std::exception& ex) {
        std::cerr << "Uncaught std::exception reached main():" << std::endl;
        std::cerr << ex.what() << std::endl;
        throw;
    } catch (...) {
        std::cerr << "Uncaught unknown exception reached main():" << std::endl;
        throw;
    }
}

