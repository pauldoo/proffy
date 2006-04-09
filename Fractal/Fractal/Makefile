objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
deps := $(patsubst %.cpp, %.dep, $(wildcard *.cpp))

CXXFLAGS = -Wall -Os -g -pipe `Magick++-config --cxxflags`
CPPFLAGS = `Magick++-config --cppflags`

all: External.h.gch ../libFractal.a

clean:
	rm -f ../libFractal.a *.o *.gch

../libFractal.a: $(objects)
	ar -rc $@ $(objects)

%.dep: %.cpp
	g++ -MM $< > $@

%.h.gch: %.h
	g++ $(CXXFLAGS) -c -o $@ $< $(CPPFLAGS)

%.o: %.cpp %.dep
	g++ $(CXXFLAGS) -c -o $@ $<

include $(deps)

