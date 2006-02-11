#ifndef Fractal_Sampler
#define Fractal_Sampler

namespace Magick { class Image; }

namespace Fractal
{
    template<typename T>
    class Sampler
    {
    public:
        typedef T Type;
        
        Sampler();
        
        virtual ~Sampler() = 0;
        
        virtual void Render(Magick::Image& image, const double& exposure) = 0;
    };
}

#endif

