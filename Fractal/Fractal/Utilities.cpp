#include "External.h"
#include "Utilities.h"

namespace Fractal {
    namespace Utilities {
        namespace {
            class Seeder
            {
            public:
                Seeder()
                {
                    srandom(time(0));
                }
            };
            
            const Seeder seeder;
        }

        const double Expose(const double& value, const double& exposure)
        {
            return 1.0 - std::exp(-value * exposure);
        }
        
        const double Random(void)
        {
            return random() / (RAND_MAX + 1.0);
        }
        
        const unsigned int Random(const unsigned int range)
        {
            assert(range <= (RAND_MAX / 10));
            return random() % range;
        }
        
        const unsigned int Random(const unsigned int min, const unsigned int max)
        {
            return min + Random(max - min + 1);
        }
    }
}

