#ifndef Fractal_Utilities
#define Fractal_Utilities

namespace Fractal
{
    namespace Utilities {
        const double Expose(const double& value, const double& exposure);
        
        /// Returns a random number in the range [0, 1)
        const double Random(void);
        
        /// Returns a random number in the range [0, range)
        const unsigned int Random(const unsigned int range);
        
        /// Returns a random number in the range [min, max]
        const unsigned int Random(const unsigned int min, const unsigned int max);
    }
}

#endif

