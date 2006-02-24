#include "External.h"

#include "Compressor.h"
#include "Decompressor.h"

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
        
    try {
        std::string output_filename;
    
        boost::program_options::options_description options("Command line options");
        options.add_options()
            ("help", "Display this help message")
            ("compress", "Compresses stdin to stdout")
            ("decompress", boost::program_options::value<std::string>(&output_filename), "Decompress stdin to specified output file");
            
        boost::program_options::variables_map variables;
        boost::program_options::store(boost::program_options::parse_command_line(argc, argv, options), variables);
        boost::program_options::notify(variables);
    
        if (variables.count("help")) {
            std::cout << options << std::endl;
            return 0;
        }
        
        if ((variables.count("compress") + variables.count("decompress")) != 1) {
            std::cerr << "Must specifiy exactly one compress or decompress flag" << std::endl;
            std::cerr << options << std::endl;
            return 1;
        }
    
        if (variables.count("compress")) {
            LRC::Compressor lrc(&std::cout, 512);
            lrc.Compress(std::cin);
            std::cout.flush();
        } else if (variables.count("decompress")) {
            std::fstream output(output_filename.c_str(), std::ios::binary | std::ios::in | std::ios::out | std::ios::trunc);
            if (!output) {
                throw std::string("Failed to open output file");
            }
            LRC::Decompressor lrc(&output);
            lrc.Decompress(std::cin);
        } else {
            throw std::string("Invalid command line");
        }
    } catch (const std::exception& ex) {
        std::cerr << "Exception: " << ex.what() << std::endl;
        return 1;
    } catch (const std::string& ex) {
        std::cerr << "Exception: " << ex << std::endl;
        return 1;
    } catch (const char* ex) {
        std::cerr << "Exception: " << ex << std::endl;
        return 1;
    } catch (...) {
        std::cerr << "Unknown exception" << std::endl;
        return 1;
    }
    return 0;
}

