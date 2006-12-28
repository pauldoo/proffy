#include "ToyTest/PrecompiledDeclarations.h"
#include "ToyTest/SimpleImage.h"

#include "Toy/SimpleImage.h"

namespace ToyTest {

    SimpleImage::SimpleImage() : UnitTest("SimpleImage")
    {
    }
    
    SimpleImage::~SimpleImage()
    {
    }
    
    void SimpleImage::Execute()
    {
        typedef Toy::SimpleImage<int, 3> ImageType;
        boost::intrusive_ptr<Toy::Image<3> > image;

        const Toy::Coordinate<unsigned int, 3> size = Toy::MakeCoordinate(2, 3, 4);
        const Toy::Coordinate<unsigned int, 3> samplePoint = Toy::MakeCoordinate(1, 2, 3);
        
        image = new ImageType(size);
        Log() << (*image) << "\n";
        Log() << image->ReadSingleValue(samplePoint) << "\n";
        image->WriteSingleValue(samplePoint, 42);
        Log() << (*image) << "\n";
        Log() << image->ReadSingleValue(samplePoint) << "\n";
        
        
        boost::intrusive_ptr<ImageType::UnsignedShortBlockType> block;
        block = image->ReadUnsignedShortRegion(Toy::MakeCoordinate(1, 1, 1), size);
        int v = 0;
        for (ImageType::UnsignedShortBlockType::iterator i = block->begin(); i != block->end(); ++i) {
            *i = ++v;
        }
        image->WriteUnsignedShortRegion(Toy::MakeCoordinate(1, 1, 1), *block);

        Log() << (*image) << "\n";
        Log() << image->ReadSingleValue(samplePoint) << "\n";

        image->WriteUnsignedShortRegion(Toy::MakeCoordinate(0, 0, 0), *block);
    }
}

