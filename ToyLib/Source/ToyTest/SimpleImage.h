#pragma once

#include "ToyTest/UnitTest.h"

namespace ToyTest {
    class SimpleImage : public UnitTest
    {
    public:
        SimpleImage();
        
        ~SimpleImage();
        
    protected:
        void Execute();
    };
}


