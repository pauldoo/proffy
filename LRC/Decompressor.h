#ifndef LRC_Decompressor
#define LRC_Decompressor

namespace LRC
{
    class Decompressor
    {
        public:
            Decompressor(std::iostream* const output);
            ~Decompressor();
            
            void Decompress(std::istream& input);
            
        private:
            std::iostream* const m_output;
    };
}

#endif
