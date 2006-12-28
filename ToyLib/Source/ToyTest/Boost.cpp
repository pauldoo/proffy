#include "ToyTest/PrecompiledDeclarations.h"
#include "ToyTest/Boost.h"

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
    
    Boost::Boost() : UnitTest("Boost")
    {
    }
    
    Boost::~Boost()
    {
    }
    
    void Boost::Execute()
    {
        boost::intrusive_ptr<TestObject> foo = new TestObject();
    }
}
