#include "Utilities.h"

#include <cmath>
#include <cstdlib>
#include <ctime>

namespace Fractal {
    namespace Utilities {
        const double Expose(const double& value, const double& exposure)
        {
            return 1.0 - std::exp(-value * exposure);
        }
        
        const double Random(void)
        {
            static bool seeded = false;
            if (!seeded) {
                srandom(time(0));
                seeded = true;
            }
            return random() / (RAND_MAX + 1.0);
        }
    }
}

