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

#ifndef LRC_Types
#define LRC_Types

namespace LRC
{
    typedef u_int8_t UInt8;
    typedef u_int16_t UInt16;
    typedef u_int32_t UInt32;
    
    typedef UInt8 Byte;
    typedef UInt32 WeakHash;
    typedef std::vector<Byte> StrongHash;
}

#endif

