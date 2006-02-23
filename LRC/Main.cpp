#include "External.h"

#include "Compressor.h"

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
    LRC::Compressor lrc(&std::cout, 512);
    lrc.WriteStream(std::cin);
    std::cout.flush();
    return 0;
}

