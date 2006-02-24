#include "External.h"

#include "Compressor.h"
#include "Decompressor.h"

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
    try {
        switch (argv[1][0]) {
            case 'c':
                {
                    LRC::Compressor lrc(&std::cout, 512);
                    lrc.Compress(std::cin);
                    std::cout.flush();
                }
                break;
            case 'd':
                {
                    std::fstream output(argv[2]);
                    LRC::Decompressor lrc(&output);
                    lrc.Decompress(std::cin);
                }
                break;
            default:
                throw "Invalid command line";
        }
    } catch (const std::exception& ex) {
        std::cerr << "Exception: " << ex.what() << std::endl;
        return 1;
    } catch (const std::string& ex) {
        std::cerr << "Exception: " << ex << std::endl;
        return 1;
    } catch (...) {
        std::cerr << "Unknown exception" << std::endl;
        return 1;
    }
    return 0;
}

