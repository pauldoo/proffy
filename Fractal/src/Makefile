objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
deps := $(patsubst %.cpp, %.dep, $(wildcard *.cpp))

all: $(objects)

clean:
	rm -f *.o

%.dep: %.cpp
	g++ -MM $< > $@

%.o: %.cpp %.dep
	g++ -Wall -Os -pipe -c -o $@ $< `Magick++-config --cppflags --cxxflags`

include $(deps)

