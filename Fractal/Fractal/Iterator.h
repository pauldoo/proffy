#ifndef Fractal_Iterator
#define Fractal_Iterator

namespace Fractal {
    template <typename T>
    class Iterator
    {
    public:
        typedef T Type;
    
        virtual ~Iterator() = 0;
        
        virtual void Seed(const Type& seed) = 0;
        virtual const Type& Value(void) const = 0;
        virtual const Type& Iterate(void) = 0;
    };
}

#endif

