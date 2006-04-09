#include "Fractal/External.h"

#include "Fractal/Geometry.h"
#include "Fractal/Utilities.h"
#include <boost/multi_array.hpp>


namespace
{
    const int width = 128;
    const int height = 128;
    const int supersample = 1;
    
    class DLA
    {
    public:
        DLA(Magick::Image& image)
          : m_width(image.size().width()),
            m_height(image.size().height()),
            m_max_density(0),
            m_densities(boost::extents[m_height][m_width])
        {
            m_centre(0) = m_width / 2;
            m_centre(1) = m_height / 2;
        }
        
        virtual ~DLA()
        {
        }
        
        void Grow(const unsigned int max_density)
        {
            {
                Fractal::iVector2 origin;
                origin(0) = 0;
                origin(1) = 0;
                Plot(origin, max_density);
            }
            std::vector<unsigned int> seed_radii(max_density + 1, 5);
            while (seed_radii[0] + 10 < std::min(m_width, m_height) / 2) {
                const double seed_angle = Fractal::Utilities::Random() * M_PI * 2;
                const unsigned int density = Fractal::Utilities::Random(1, max_density);

                Fractal::iVector2 point;
                point(0) = static_cast<unsigned int>(cos(seed_angle) * seed_radii[density]);
                point(1) = static_cast<unsigned int>(sin(seed_angle) * seed_radii[density]);
                //std::cout << "New point: " << point(0) << ", " << point(1) << std::endl;
                while (Radius(point) < seed_radii[density] + 5) {
                    //std::cout << "(" << point(0) << ", " << point(1) << ")\t";
                    switch(RandomDirection()) {
                        case 0:
                            point(0) -= 1;
                            break;
                        case 1:
                            point(0) += 1;
                            break;
                        case 2:
                            point(1) -= 1;
                            break;
                        case 3:
                            point(1) += 1;
                            break;
                        default:
                            assert(false);
                    }
                    if (Touching(point, density)) {
                        Plot(point, density);
                        for (unsigned int i = 0; i <= density; i++) {
                            seed_radii[i] = std::max(Radius(point) + 5, seed_radii[i]);
                        }
                        break;
                    }
               }
                //std::cout << std::endl;
            }
        }
        
        void Render(Magick::Image& image)
        {
            assert(image.size().width() == m_width);
            assert(image.size().height() == m_height);
            std::cout << m_max_density << std::endl;
            
            for (unsigned int x = 0; x < m_width; x++) {
                for (unsigned int y = 0; y < m_height; y++) {
                    //std::cout << "(" << m_densities[x][y];
                    const double v = static_cast<double>(m_densities[x][y]) / m_max_density;
                    //std::cout << ", " << v << ")\t";
                    image.pixelColor( x, y, Magick::ColorGray(v) );
                }
            }
        }
        
    private:
        const int RandomDirection(void)
        {
        }
    
        const bool Touching(const Fractal::iVector2& point, const unsigned int density)
        {
            const Fractal::iVector2 real_point = point + m_centre;
            if (m_densities[real_point(0) - 1][real_point(1)] >= density) {
                return true;
            }
            if (m_densities[real_point(0) + 1][real_point(1)] >= density) {
                return true;
            }
            if (m_densities[real_point(0)][real_point(1) - 1] >= density) {
                return true;
            }
            if (m_densities[real_point(0)][real_point(1) + 1] >= density) {
                return true;
            }
            return false;
        }
    
        const unsigned int Radius(const Fractal::iVector2& point)
        {
            return static_cast<unsigned int>(sqrt(point(0) * point(0) + point(1) * point(1)));
        }
    
        void Plot(const Fractal::iVector2& point, const unsigned int density)
        {
            m_max_density = std::max(m_max_density, density);
            //std::cout << point(0) << "\t" << point(1) << std::endl;
            //std::cout << ".";
            const Fractal::iVector2 real_point = point + m_centre;
            m_densities[real_point(0)][real_point(1)] = std::max(m_densities[real_point(0)][real_point(1)], density);
        }

    
        const unsigned int m_width;
        const unsigned int m_height;
        unsigned int m_max_density;
        Fractal::iVector2 m_centre;
        boost::multi_array<unsigned int, 2> m_densities;
        
        unsigned int m_random_data;
        unsigned int m_random_bits;
    };
    
    void ProduceDLAFractal(Magick::Image& image)
    {
        DLA dla(image);
        dla.Grow(100);
        dla.Render(image);
    }
}

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
    
    Magick::Image image(Magick::Geometry(width * supersample, height * supersample), Magick::ColorRGB(0.0, 0.0, 0.0));
    
    ProduceDLAFractal(image);

    image.scale(Magick::Geometry(width, height));
    image.write("images/dla_image.bmp");
    
    return 0;
}

