objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
deps := $(patsubst %.cpp, %.dep, $(wildcard *.cpp))

all: ../libFractal.a

clean:
	rm -f ../libFractal.a *.o

../libFractal.a: $(objects)
	ar -rc $@ $(objects)

%.dep: %.cpp
	g++ -MM $< > $@

%.o: %.cpp %.dep
	g++ -Wall -Os -pipe -c -o $@ $< -I.. `Magick++-config --cppflags --cxxflags`

include $(deps)

