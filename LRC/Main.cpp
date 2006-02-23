#include "External.h"

#include "LRC.h"

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
    LRC lrc(&std::cout);
    lrc.WriteStream(std::cin);
    std::cout.flush();
}

