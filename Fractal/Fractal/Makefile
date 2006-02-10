objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
deps := $(patsubst %.cpp, %.dep, $(wildcard *.cpp))

all: ../Fractal.a

Fractal.a: $(objects)
	ar -rc $@ $<

clean:
	rm -f *.o

%.dep: %.cpp
	g++ -MM $< > $@

%.o: %.cpp %.dep
	g++ -Wall -Os -pipe -c -o $@ $< `Magick++-config --cppflags --cxxflags`

include $(deps)

