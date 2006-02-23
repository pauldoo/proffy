#ifndef LRC_RollingChecksum
#define LRC_RollingChecksum

#include "Types.h"

namespace LRC
{

    class RollingChecksum
    {
        public:
            RollingChecksum(const unsigned int block_size);
            ~RollingChecksum();
            
            void NextByte(const Byte);
            
            const WeakHash WeakChecksum(void) const;
            
            const StrongHash StrongChecksum(void) const;
            
        private:
            UInt16 m_rolling_a;
            UInt16 m_rolling_b;
            unsigned int m_offset;
            std::vector<Byte> m_buffer;
    };
}

#endif

