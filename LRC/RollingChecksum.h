// Long Range Compressor (LRC)
// Copyright (C) 2006  Paul Richards
// 
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

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

