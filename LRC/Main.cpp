// Long Range Compressor (LRC)
// Copyright (C) 2006  Paul Richards
// 
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

#include "External.h"

#include "Compressor.h"
#include "Decompressor.h"

namespace LRC
{
    void PrintLicence(std::ostream& out)
    {
        out
            << "LRC version 0.1, Copyright (C) 2006 Paul Richards" << std::endl
            << "LRC comes with ABSOLUTELY NO WARRANTY; for details see COPYRIGHT.txt." << std::endl
            << "This is free software, and you are welcome to redistribute it" << std::endl
            << "under certain conditions; see COPYRIGHT.txt for details." << std::endl
            << std::endl;
    }
    
    void PrintUsage(std::ostream& out, const boost::program_options::options_description& options)
    {
        PrintLicence(out);
        out << options << std::endl;
    }
}

int main(int argc, char* argv[])
{
    std::ios::sync_with_stdio(false);
        
    try {
        std::string output_filename;
        unsigned int block_size;
    
        boost::program_options::options_description basic_options("Basic options");
        basic_options.add_options()
            ("help,h", "Display this help message.")
            ("compress,c", "Compresses stdin to stdout.")
            ("decompress,d", boost::program_options::value<std::string>(&output_filename), "Decompress stdin to specified output file.")
            ("quiet,q", "Suppress copyright preamble.");
            
        boost::program_options::options_description advanced_options("Advanced options");
        advanced_options.add_options()
            ("blocksize", boost::program_options::value<unsigned int>(&block_size)->default_value(512), "Block size used when compressing (in bytes).  Smaller values may give higher compression at the cost of using more memory.");

        boost::program_options::options_description all_options("Command line options");
        all_options.add(basic_options).add(advanced_options);
            
        boost::program_options::variables_map variables;
        boost::program_options::store(boost::program_options::parse_command_line(argc, argv, all_options), variables);
        boost::program_options::notify(variables);
        
        if (variables.count("help")) {
            LRC::PrintUsage(std::cout, all_options);
            return EXIT_SUCCESS;
        }

        if (!variables.count("quiet")) {
            LRC::PrintLicence(std::clog);
        }
        
        if ((variables.count("compress") + variables.count("decompress")) != 1) {
            std::clog << "Must specifiy exactly one compress or decompress flag, use --help for details." << std::endl;
            return EXIT_FAILURE;
        }
        
        if (variables.count("compress")) {
            LRC::Compressor lrc(&std::cout, block_size);
            lrc.Compress(std::cin);
            std::cout.flush();
        } else if (variables.count("decompress")) {
            if (variables.count("blocksize")) {
                std::clog << "The blocksize flag is only applicable when compressing." << std::endl;
                return EXIT_FAILURE;
            }
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
        return EXIT_FAILURE;
    } catch (const std::string& ex) {
        std::cerr << "Exception: " << ex << std::endl;
        return EXIT_FAILURE;
    } catch (const char* ex) {
        std::cerr << "Exception: " << ex << std::endl;
        return EXIT_FAILURE;
    } catch (...) {
        std::cerr << "Unknown exception" << std::endl;
        return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
}

