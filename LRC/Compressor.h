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

#ifndef LRC_Compressor
#define LRC_Compressor

#include "Types.h"

namespace LRC { class RollingChecksum; }

namespace LRC
{
    class Compressor
    {
        public:
            Compressor(std::ostream* const output, const unsigned int block_size);
            ~Compressor();
    
            void Compress(std::istream& input);
        
        private:
            typedef std::map<StrongHash, unsigned int> StrongMap;
            typedef std::map<WeakHash, StrongMap> WeakMap;
            enum eMode { eMode_Block, eMode_Raw };

            void WriteRaw(const Byte);
            
            void SnipRawBuffer(void);
            
            void FlushRaw(void);
            
            void WriteBlock(const unsigned int offset);
            
            std::ostream* const m_output;
            const unsigned int m_block_size;

            unsigned int m_bytes_read;
            unsigned int m_bytes_to_skip;
            eMode m_mode;
            std::auto_ptr<RollingChecksum> m_checker;
            std::auto_ptr<std::vector<Byte> > m_current_raw00;
	    std::auto_ptr<WeakMap> m_previous_blocks;
    };
}

#endif

