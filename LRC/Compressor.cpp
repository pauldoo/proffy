#include "External.h"
#include "Compressor.h"

#include "RollingChecksum.h"

namespace LRC
{
    Compressor::Compressor(std::ostream* const output, const unsigned int block_size)
      : m_output(output),
        m_block_size(block_size),
        m_bytes_read(0),
        m_bytes_to_skip(0),
        m_mode(eMode_Raw),
        m_checker(new RollingChecksum(m_block_size)),
        m_current_raw00(0)
    {
    }
    
    Compressor::~Compressor()
    {
    }
    
    void Compressor::WriteRaw(const Byte value)
    {
        if (m_current_raw00.get() == 0) {
            m_current_raw00.reset(new std::vector<Byte>());
        }
        m_current_raw00->push_back(value);
    }
    
    void Compressor::SnipRawBuffer()
    {
        if (m_current_raw00.get() != 0) {
            m_current_raw00->resize( m_current_raw00->size() - m_block_size );
        }
    }
    
    void Compressor::FlushRaw()
    {
        if (m_current_raw00.get() != 0 && m_current_raw00->size() > 0) {
            if (m_mode == eMode_Block) {
                const int mark = -1;
                m_output->write(reinterpret_cast<const char*>(&mark), sizeof(int));
            }
            const unsigned int size = m_current_raw00->size();
            m_output->write(reinterpret_cast<const char*>(&size), sizeof(unsigned int));
            m_output->write(reinterpret_cast<const char*>(&(m_current_raw00->front())), size);
            m_mode = eMode_Raw;
        }
        m_current_raw00.reset();
    }
    
    void Compressor::WriteBlock(const unsigned int offset)
    {
        FlushRaw();
        m_output->write(reinterpret_cast<const char*>(&offset), sizeof(unsigned int));
        m_mode = eMode_Block;
    }
    
    void Compressor::WriteStream(std::istream& input)
    {
        while (true) {
            const Byte value = static_cast<Byte>(input.get());
            if (!input) {
                break;
            }
            m_bytes_read++;
            m_checker->NextByte(value);
            WriteRaw(value);
            const WeakHash weak_checksum = m_checker->WeakChecksum();
            StrongHash strong_checksum;

            if (m_bytes_to_skip > 0) {
                m_bytes_to_skip--;
            } else {
                WeakMap::const_iterator weak_matches = m_previous_blocks.find(weak_checksum);
                if (weak_matches != m_previous_blocks.end()) {
                    strong_checksum = m_checker->StrongChecksum();
                    StrongMap::const_iterator strong_match = weak_matches->second.find(strong_checksum);
                    if (strong_match != weak_matches->second.end()) {
                        SnipRawBuffer();
                        WriteBlock(strong_match->second);
                        m_bytes_to_skip = m_block_size - 1;
                    }
                }
            }
            
            if ((m_bytes_read % m_block_size) == 0) {
                StrongMap& weak_matches = m_previous_blocks[weak_checksum];
                if (strong_checksum.empty()) {
                    strong_checksum = m_checker->StrongChecksum();
                }
                if (weak_matches.find(strong_checksum) == weak_matches.end()) {
                    weak_matches[strong_checksum] = m_bytes_read - m_block_size;
                }
            }
        }
        FlushRaw();
    }
}

