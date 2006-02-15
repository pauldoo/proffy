objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
bins = $(patsubst %.cpp, %, $(wildcard *.cpp))

LIBS = ../libFractal.a -lboost_program_options `Magick++-config --ldflags --libs`

all: $(objects) $(bins)

clean:
	rm -f $(bins) *.o

%: %.o ../libFractal.a
	g++ -Wall -O3 -g -pipe $< -o $@ $(LIBS)

%.o: %.cpp
	g++ -Wall -O3 -g -pipe `Magick++-config --cxxflags` -c -o $@ $< -I ..


