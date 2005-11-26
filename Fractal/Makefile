targets := fractal images/mandelbrot_8.pnm images/julia_8.pnm images/mandelbrot_accum_8.pnm images/julia_accum_8.pnm
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

images/mandelbrot_accum_16.pnm: images/mandelbrot_16.pnm
images/julia_accum_16.pnm: images/julia_16.pnm

images/mandelbrot_16.pnm: fractal
	./fractal --width 2048 --height 1536 --output $@ --accum_output images/mandelbrot_accum_16.pnm

images/julia_16.pnm: fractal
	./fractal --width 2048 --height 1536 --julia_real 0.3 --julia_imag 0.6 --output $@ --accum_output images/julia_accum_16.pnm

