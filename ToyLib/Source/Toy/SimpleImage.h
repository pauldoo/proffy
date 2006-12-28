#pragma once

#include "Toy/Image.h"
#include "Toy/Block.h"

namespace Toy {
    template<typename TYPE, unsigned int DIMENSIONS>
    class SimpleImage : public Image<DIMENSIONS>
    {
    public:
        typedef Image<DIMENSIONS> ImageType;
        
        SimpleImage(const typename ImageType::CoordinateType& size) :
            m_data_block(new BlockType(size))
        {
        }

        SimpleImage(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::CoordinateType& end) :
            m_origin(begin),
            m_data_block(new BlockType(end - begin))
        {
        }
        
        const typename ImageType::CoordinateType Begin() const
        {
            return m_origin;
        }
        
        const typename ImageType::CoordinateType End() const
        {
            return m_origin + m_data_block->Size();
        }
        
        const typename ImageType::CoordinateType Size() const
        {
            return m_data_block->Size();
        }
        
        const boost::intrusive_ptr<typename ImageType::SignedShortBlockType> ReadSignedShortRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::CoordinateType& end
        ) const
        {
            return ReadRegion<signed short>(begin, end);
        }

        const boost::intrusive_ptr<typename ImageType::UnsignedShortBlockType> ReadUnsignedShortRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::CoordinateType& end
        ) const
        {
            return ReadRegion<unsigned short>(begin, end);
        }

        const boost::intrusive_ptr<typename ImageType::FloatBlockType> ReadFloatRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::CoordinateType& end
        ) const
        {
            return ReadRegion<float>(begin, end);
        }
        
        void WriteSignedShortRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::SignedShortBlockType& values
        )
        {
            return WriteRegion(begin, values);
        }

        void WriteUnsignedShortRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::UnsignedShortBlockType& values
        )
        {
            return WriteRegion(begin, values);
        }
        
        void WriteFloatRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::FloatBlockType& values
        )
        {
            return WriteRegion(begin, values);
        }
        
    protected:
        ~SimpleImage()
        {
        }
        
    private:
        typedef Block<TYPE, DIMENSIONS> BlockType;

        template<typename OTHERTYPE>
        const boost::intrusive_ptr<Block<OTHERTYPE, DIMENSIONS> > ReadRegion(
            const typename ImageType::CoordinateType& begin,
            const typename ImageType::CoordinateType& end
        ) const
        {
            TOY_ASSERT(end.EntirelyContains(begin), "Non-positive volume");
            typedef Block<OTHERTYPE, DIMENSIONS> ResultType;
            boost::intrusive_ptr<ResultType> result = new ResultType(end - begin);
            
            const unsigned int pixelSpanLength = *(end.end()-1) - *(begin.end()-1);
            typename BlockType::CoordinateType position = begin;
            for (typename ResultType::iterator out = result->begin(); out != result->end(); out += pixelSpanLength) {
                typename BlockType::const_iterator spanStart = m_data_block->Lookup(position);
                std::copy(spanStart, spanStart + pixelSpanLength, out);

                ++position[DIMENSIONS-2];
                for (unsigned int i = DIMENSIONS-2; i >= 0 && position[i] == end[i]; --i) {
                    position[i] = begin[i];
                    if (i > 0) {
                        ++position[i-1];
                    }
                }
            }
            
            return result;
        }
        
        template<typename OTHERTYPE>
        void WriteRegion(
            const typename ImageType::CoordinateType& begin,
            const Block<OTHERTYPE, DIMENSIONS>& values
        )
        {
            TOY_ASSERT(!Begin().EntirelyContains(begin), "Write block is outwith image");
            TOY_ASSERT(!(begin + values.Size()).EntirelyContains(End()), "Write block is outwith image");
            typedef Block<OTHERTYPE, DIMENSIONS> InType;
            
            const unsigned int pixelSpanLength = *(values.Size().end()-1);
            typename BlockType::CoordinateType position = begin;
            for (typename InType::const_iterator in = values.begin(); in != values.end(); in += pixelSpanLength) {
                typename BlockType::iterator out = m_data_block->Lookup(position);
                std::copy(in, in + pixelSpanLength, out);

                ++position[DIMENSIONS-2];
                for (unsigned int i = DIMENSIONS-2; i >= 0 && position[i] == (begin[i] + values.Size()[i]); --i) {
                    position[i] = begin[i];
                    if (i > 0) {
                        ++position[i-1];
                    }
                }
            }
        }

        typename ImageType::CoordinateType m_origin;
        boost::intrusive_ptr<BlockType> m_data_block;
    };
}

