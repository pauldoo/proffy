bins = $(patsubst %.cpp, %, $(wildcard *.cpp))

LIBS = ../libFractal.a -lboost_program_options `Magick++-config --ldflags --libs`

all: $(bins)

clean:
	rm -f $(bins)

%: %.cpp ../libFractal.a
	g++ -Wall -Os -pipe -o $@ $< -I.. $(LIBS)

