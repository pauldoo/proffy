objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
bins = $(patsubst %.cpp, %, $(wildcard *.cpp))

LIBS = ../libFractal.a -lboost_program_options `Magick++-config --ldflags --libs`

all: $(objects) $(bins)

clean:
	rm -f $(bins) *.o

%: %.o ../libFractal.a
	g++ -Wall -Os -g -pipe $< -o $@ $(LIBS)

%.o: %.cpp
	g++ -Wall -Os -g -pipe `Magick++-config --cxxflags` -c -o $@ $< -I ..


