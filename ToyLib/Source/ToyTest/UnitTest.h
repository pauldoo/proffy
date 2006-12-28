#pragma once

namespace ToyTest {
    class UnitTest
    {
    public:
        UnitTest(const std::string& name);
        virtual ~UnitTest() = 0;
        
        void Run();
        
        void Check(const bool condition, const std::string& message) const;
        
    protected:
        virtual void Execute() = 0;
        
        std::ostream& Log();
        
    private:
        const std::string m_name;
        std::ostream& m_log;
    };
}


