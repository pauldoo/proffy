#pragma once

#include "ToyTest/UnitTest.h"

namespace ToyTest {
    class Block : public UnitTest
    {
    public:
        Block();
        
        ~Block();
        
    protected:
        void Execute();
    };
}


