#include "Utilities.h"

#include <cmath>

namespace Fractal {
    namespace Utilities {
        double Expose(const double& value, const double& exposure)
        {
            return 1.0 - std::exp(-value * exposure);
        }
    }
}

