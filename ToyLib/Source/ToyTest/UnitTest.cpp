#include "ToyTest/PrecompiledDeclarations.h"
#include "ToyTest/UnitTest.h"

#include "Toy/AssertException.h"

namespace ToyTest {
    UnitTest::UnitTest(const std::string& name) :
        m_name(name),
        m_log(std::cout)
    {
    }
    
    UnitTest::~UnitTest()
    {
    }
    
    void UnitTest::Run()
    {
        try {
            Log() << "Testing: " << m_name << "\n";
            Execute();
        } catch (std::exception& ex) {
            Log() << "\n\n";
            Log() << "Failed with: " << typeid(ex).name() << "\n";
            Log() << ex.what() << "\n";
            Log() << "\n\n";
        } catch (...) {
            Log() << "\n\n";
            Log() << "Failed with unknown exception\n";
            Log() << "\n\n";
        }
    }
    
    void UnitTest::Check(const bool condition, const std::string& message) const
    {
        TOY_ASSERT(condition, message);
    }
    
    std::ostream& UnitTest::Log()
    {
        return m_log;
    }
}

