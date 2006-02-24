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
#include "Decompressor.h"

namespace LRC
{
    Decompressor::Decompressor(std::iostream* const output)
      : m_output(output)
    {
    }
    
    Decompressor::~Decompressor()
    {
    }
    
    void Decompressor::Decompress(std::istream& input)
    {
        while (true) {
            unsigned int raw_size;
            input.read(reinterpret_cast<char*>(&raw_size), sizeof(unsigned int));
            if (!input) {
                return;
            }
            {
		//std::cerr << "Raw: " << raw_size << std::endl;
                std::vector<char> buffer(raw_size);
                input.read(&(buffer.front()), raw_size);
                m_output->write(&(buffer.front()), raw_size);
		if (!(*m_output)) {
		    throw std::string("Write failed");
		}
            }
            
            while (true) {
                unsigned int repeat_offset;
                input.read(reinterpret_cast<char*>(&repeat_offset), sizeof(unsigned int));
                if (!input) {
                    return;
                }
                if (repeat_offset == static_cast<unsigned int>(-1)) {
                    break;
                }
                unsigned int repeat_size;
                input.read(reinterpret_cast<char*>(&repeat_size), sizeof(unsigned int));

		//std::cerr << "Block: " << repeat_offset << " : " << repeat_size << std::endl;
                
                const unsigned int head = m_output->tellg();
                m_output->seekg(repeat_offset);
                std::vector<char> buffer(repeat_size);
                m_output->read(&(buffer.front()), repeat_size);
                m_output->seekg(head);
                m_output->write(&(buffer.front()), repeat_size);
		if (!(*m_output)) {
		    throw std::string("Write failed");
		}
            }
        }
    }
}
