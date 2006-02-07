objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))

all: Makefile.dep $(objects)

dep: Makefile.dep

clean:
	rm -f *.o

Makefile.dep: $(wildcard *.cpp)
	g++ -MM $< > $@

%.o: %.cpp
	g++ -Wall -Os -pipe -c -o $@ $< `Magick++-config --cppflags --cxxflags`

include Makefile.dep

