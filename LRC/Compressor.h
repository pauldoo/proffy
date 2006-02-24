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
            std::auto_ptr< std::vector<Byte> > m_current_raw00;
            WeakMap m_previous_blocks;
    };
}

#endif

