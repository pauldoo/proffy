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
    
        boost::program_options::options_description options("Command line options");
        options.add_options()
            ("help", "Display this help message")
            ("compress", "Compresses stdin to stdout")
            ("decompress", boost::program_options::value<std::string>(&output_filename), "Decompress stdin to specified output file")
            ("quiet", "Suppress copyright preamble");
            
        boost::program_options::variables_map variables;
        boost::program_options::store(boost::program_options::parse_command_line(argc, argv, options), variables);
        boost::program_options::notify(variables);
        
        if (variables.count("help")) {
            LRC::PrintUsage(std::cout, options);
            return 0;
        }

        if (!variables.count("quiet")) {
            LRC::PrintLicence(std::clog);
        }
        
        if ((variables.count("compress") + variables.count("decompress")) != 1) {
            std::clog << "Must specifiy exactly one compress or decompress flag, use --help for details" << std::endl;
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

