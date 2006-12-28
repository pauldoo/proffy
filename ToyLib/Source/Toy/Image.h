#pragma once

#include "Toy/Block.h"
#include "Toy/Coordinate.h"
#include "Toy/Counted.h"

namespace Toy {
    template<unsigned int DIMENSIONS>
    class Image : public Toy::Counted
    {
    public:
        typedef Coordinate<unsigned int, DIMENSIONS> CoordinateType;
        typedef Block<signed short, DIMENSIONS> SignedShortBlockType;
        typedef Block<unsigned short, DIMENSIONS> UnsignedShortBlockType;
        typedef Block<float, DIMENSIONS> FloatBlockType;
        
        Image()
        {
        }
        
        const unsigned int Dimensions() const
        {
            return DIMENSIONS;
        }
        
        virtual const CoordinateType Begin() const = 0;
        
        virtual const CoordinateType End() const = 0;
        
        virtual const CoordinateType Size() const = 0;
        
        const float ReadSingleValue(const CoordinateType& position) const
        {
            const boost::intrusive_ptr<const FloatBlockType> block = ReadFloatRegion(position, position + CoordinateType::Ones());
            return *(block->begin());
        }
        
        void WriteSingleValue(const CoordinateType& position, const float value)
        {
            const boost::intrusive_ptr<FloatBlockType> block = ReadFloatRegion(position, position + CoordinateType::Ones());
            *(block->begin()) = value;
            WriteFloatRegion(position, *block);
        }
        
        virtual const boost::intrusive_ptr<SignedShortBlockType> ReadSignedShortRegion(
            const CoordinateType& begin,
            const CoordinateType& end
        ) const = 0;

        virtual const boost::intrusive_ptr<UnsignedShortBlockType> ReadUnsignedShortRegion(
            const CoordinateType& begin,
            const CoordinateType& end
        ) const = 0;

        virtual const boost::intrusive_ptr<FloatBlockType> ReadFloatRegion(
            const CoordinateType& begin,
            const CoordinateType& end
        ) const = 0;
        
        virtual void WriteSignedShortRegion(
            const CoordinateType& begin,
            const SignedShortBlockType& values
        ) = 0;

        virtual void WriteUnsignedShortRegion(
            const CoordinateType& begin,
            const UnsignedShortBlockType& values
        ) = 0;
        
        virtual void WriteFloatRegion(
            const CoordinateType& begin,
            const FloatBlockType& values
        ) = 0;
        
    protected:
        virtual ~Image() = 0;
    };
    
    template<unsigned int DIMENSIONS>
    Image<DIMENSIONS>::~Image()
    {
    }
    
    template<unsigned int DIMENSIONS>
    std::ostream& operator << (std::ostream& out, const Image<DIMENSIONS>& image)
    {
        const unsigned int PIXEL_COUNT_LIMIT = 200;
        const unsigned int pixelCount = image.Size().BlockVolume();
        if (pixelCount > PIXEL_COUNT_LIMIT) {
            out << "Image " << image.ClassName() << " has too many pixels to print (" << pixelCount << ")";
        } else {
            out << *(image.ReadFloatRegion(image.Begin(), image.End()));
        }
        return out;
    }
}

