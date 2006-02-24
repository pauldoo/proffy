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

#include "External.h"
#include "RollingChecksum.h"

namespace LRC
{
    RollingChecksum::RollingChecksum(const unsigned int block_size)
      : m_rolling_a(0),
	m_rolling_b(0),
	m_offset(0),
        m_buffer(block_size)
    {
    }
    
    RollingChecksum::~RollingChecksum()
    {
    }
    
    void RollingChecksum::NextByte(const Byte new_byte)
    {
        const Byte old_byte = m_buffer[m_offset];
        m_buffer[m_offset] = new_byte;
        m_offset = (m_offset + 1) % m_buffer.size();
        
        m_rolling_a = m_rolling_a - old_byte + new_byte;
        m_rolling_b = m_rolling_b - (m_buffer.size() * old_byte) + m_rolling_a;
    }
    
    const WeakHash RollingChecksum::WeakChecksum() const
    {
        return (static_cast<UInt32>(m_rolling_b) << 16) | m_rolling_a;
    }
    
    const StrongHash RollingChecksum::StrongChecksum(void) const
    {
        CryptoPP::MD4 hasher;
        hasher.Update(&(m_buffer[m_offset]), m_buffer.size() - m_offset);
        hasher.Update(&(m_buffer[0]), m_offset);
        StrongHash result(hasher.DigestSize());
        hasher.Final(&(result.front()));
        return result;
    }
}

