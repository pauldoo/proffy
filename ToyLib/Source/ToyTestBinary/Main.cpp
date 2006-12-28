#include "ToyTestBinary/PrecompiledDeclarations.h"
#include "ToyTest/Block.h"
#include "ToyTest/Boost.h"
#include "ToyTest/SimpleImage.h"

int main(void)
{
    ToyTest::Block().Run();
    ToyTest::Boost().Run();
    ToyTest::SimpleImage().Run();
    return 0;
}

