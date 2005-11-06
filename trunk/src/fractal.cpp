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

class MandelbrotIterator
{
    public:
    MandelbrotIterator(const std::complex<double>& c, const std::complex<double>& z0)
      : m_c(c),
        m_z(z0)
    {
    }
    
    ~MandelbrotIterator()
    {
    };
    
    void Iterate(void)
    {
        //m_z = m_z * m_z + m_c;
        m_z = sin(m_z) * m_c;
    }
    
    bool Escaped(void)
    {
        return abs(m_z) > 1e6;
    }
    
    int IterateUntilEscaped(const int max_iter)
    {
        int count = 0;
        while (count < max_iter && !Escaped()) {
            Iterate();
            count++;
        }
        if (count == max_iter) {
            count = 0;
        }
        return count;
    }

    private:
    const std::complex<double> m_c;
    std::complex<double> m_z;
};


void ProduceJuliaRendering(
    Magick::Image& image,
    const std::complex<double> top_left,
    const std::complex<double> bottom_right,
    const std::complex<double> c,
    const double exposure)
{
    const int width = image.size().width();
    const int height = image.size().height();
    const int max_iter = static_cast<int>(image.depth() * log(2) / exposure + 1);
    boost::progress_display progress( height );
    
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            const std::complex<double> z(
                top_left.real() + (x + 0.5) / width * (bottom_right.real() - top_left.real()),
                top_left.imag() + (y + 0.5) / height * (bottom_right.imag() - top_left.imag())
            );
            MandelbrotIterator it(c, z);
            const int count = it.IterateUntilEscaped(max_iter);
            const double v = 1.0 - std::exp(-count * exposure);
            image.pixelColor(x, y, Magick::ColorGray(v));
        }
        ++progress;
    }
}

void ProduceMandelbrotRendering(
    Magick::Image& image,
    const std::complex<double> top_left,
    const std::complex<double> bottom_right,
    const double exposure)
{
    const int width = image.size().width();
    const int height = image.size().height();
    const int max_iter = static_cast<int>(image.depth() * log(2) / exposure + 1);
    boost::progress_display progress( height );
    
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            const std::complex<double> c(
                top_left.real() + (x + 0.5) / width * (bottom_right.real() - top_left.real()),
                top_left.imag() + (y + 0.5) / height * (bottom_right.imag() - top_left.imag())
            );
            MandelbrotIterator it(c, c);
            const int count = it.IterateUntilEscaped(max_iter);
            const double v = 1.0 - std::exp(-count * exposure);
            image.pixelColor(x, y, Magick::ColorGray(v));
        }
        ++progress;
    }
}

int main(int argc, char* argv[])
{
    try {
        std::ios::sync_with_stdio(false);
        int width, height, supersample;
        double escape, exposure, left, right, top, bottom;
        std::string output;
        
        boost::program_options::options_description general_options("Image options");
        general_options.add_options()
            ("help", "produce help message")
            ("width", boost::program_options::value<int>(&width)->default_value(640), "width of output image in pixels")
            ("height", boost::program_options::value<int>(&height)->default_value(480), "height of output image in pixels")
            ("exposure", boost::program_options::value<double>(&exposure)->default_value(0.05), "exposure sensitivity")
            ("output", boost::program_options::value<std::string>(&output)->default_value("output.pnm"), "output image file")
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
        
        if (variables.count("julia_real")) {
            const std::complex<double> c(
                variables["julia_real"].as<double>(),
                variables["julia_imag"].as<double>()
            );
            ProduceJuliaRendering(image, top_left, bottom_right, c, exposure);
        } else {
            ProduceMandelbrotRendering(image, top_left, bottom_right, exposure);
        }
        image.scale(Magick::Geometry(width, height));
        image.write(output);
        
    } catch (std::exception& ex) {
        std::cerr << "Uncaught std::exception reached main():" << std::endl;
        std::cerr << ex.what() << std::endl;
        throw;
    } catch (...) {
        std::cerr << "Uncaught unknown exception reached main():" << std::endl;
        throw;
    }
}

