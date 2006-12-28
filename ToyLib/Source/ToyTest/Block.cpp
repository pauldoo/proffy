#include "ToyTest/PrecompiledDeclarations.h"
#include "ToyTest/Block.h"

#include "Toy/AssertException.h"
#include "Toy/Block.h"
#include "Toy/Counted.h"

namespace ToyTest {
    namespace {
        class TestObject : public Toy::Counted
        {
        public:
            TestObject()
            {
            }
            
            ~TestObject()
            {
            }
        };
    }
    
    Block::Block() : UnitTest("Block")
    {
    }
    
    Block::~Block()
    {
    }
    
    void Block::Execute()
    {
        typedef Toy::Block<float, 5> BlockType;
        Toy::Coordinate<unsigned int, 5> size = Toy::MakeCoordinate(3u, 4u, 5u, 6u, 7u);
        boost::intrusive_ptr<BlockType> foo = new BlockType(size);
        const BlockType::const_iterator base = foo->begin();
        Check(base == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 0u, 0u, 0u)), "Lookup returned incorrect value at origin");

        Check(base+1 == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 0u, 0u, 1u)), "Lookup returned incorrect value");
        Check(base+2 == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 0u, 0u, 2u)), "Lookup returned incorrect value");
        Check(base+7 == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 0u, 1u, 0u)), "Lookup returned incorrect value");
        Check(base+14 == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 0u, 2u, 0u)), "Lookup returned incorrect value");
        Check(base+42 == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 1u, 0u, 0u)), "Lookup returned incorrect value");
        Check(base+84 == foo->Lookup(Toy::MakeCoordinate(0u, 0u, 2u, 0u, 0u)), "Lookup returned incorrect value");
        Check(base+210 == foo->Lookup(Toy::MakeCoordinate(0u, 1u, 0u, 0u, 0u)), "Lookup returned incorrect value");
        Check(base+420 == foo->Lookup(Toy::MakeCoordinate(0u, 2u, 0u, 0u, 0u)), "Lookup returned incorrect value");
        Check(base+840 == foo->Lookup(Toy::MakeCoordinate(1u, 0u, 0u, 0u, 0u)), "Lookup returned incorrect value");
        Check(base+1680 == foo->Lookup(Toy::MakeCoordinate(2u, 0u, 0u, 0u, 0u)), "Lookup returned incorrect value");
        
        Check(foo->end()-1 == foo->Lookup(Toy::MakeCoordinate(2u, 3u, 4u, 5u, 6u)), "Lookup returned incorrect value at end");
    }
}
