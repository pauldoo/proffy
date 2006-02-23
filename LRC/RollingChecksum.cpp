#include "External.h"
#include "RollingChecksum.h"

namespace LRC
{
    RollingChecksum::RollingChecksum(const unsigned int block_size)
      : m_buffer(block_size)
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

