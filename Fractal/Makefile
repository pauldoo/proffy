targets := fractal images/mandelbrot_8.pnm images/julia_8.pnm
objects := $(patsubst src/%.cpp, obj/%.o, $(wildcard src/*.cpp))

all: $(targets)

clean:
	rm -f fractal images/* obj/*

fractal: $(objects)
	g++ -Wall -O3 -pipe -o $@ $(objects) -lboost_program_options `Magick++-config --ldflags --libs`

obj/%.o: src/%.cpp
	g++ -Wall -O3 -pipe -c -o $@ $< `Magick++-config --cppflags --cxxflags`


%_8.pnm: %_16.pnm
	cat $< | pnmdepth 255 > $@

images/mandelbrot_16.pnm: fractal
	./fractal --output $@

images/julia_16.pnm: fractal
	./fractal --julia_real 0.0 --julia_imag 1.0 --output $@

