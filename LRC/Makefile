# Long Range Compressor (LRC)
# Copyright (C) 2006  Paul Richards
# 
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
deps := $(patsubst %.cpp, %.dep, $(wildcard *.cpp))

CXXFLAGS = -Wall -O2 -g -pipe
CPPFLAGS =
LIBS = -lboost_program_options -lcrypto++

all: External.h.gch lrc

clean:
	rm -f lrc *.o *.gch

lrc: $(objects)
	$(CXX) $(CXXFLAGS) -o $@ $(objects) $(LIBS)

%.dep: %.cpp
	$(CXX) -MM $< > $@ $(CPPFLAGS)

%.h.gch: %.h
	$(CXX) $(CXXFLAGS) -c -o $@ $< $(CPPFLAGS)

%.o: %.cpp %.dep
	$(CXX) $(CXXFLAGS) -c -o $@ $< $(CPPFLAGS)

include $(deps)

