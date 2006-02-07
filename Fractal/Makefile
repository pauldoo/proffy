all: fractal images/mandelbrot.bmp images/julia.bmp images/mandelbrot_accum.bmp images/julia_accum.bmp

dep:
	+make -C src dep

clean:
	+make -C src clean
	rm -f fractal images/*

fractal: $(wildcard src/*.h) $(wildcard src/*.cpp)
	+make -C src
	g++ -Wall -Os -pipe -o $@ src/*.o -lboost_program_options `Magick++-config --ldflags --libs`


images/%_accum.bmp: images/%.bmp

images/mandelbrot.bmp: fractal
	./fractal --width 512 --height 384 --output $@ --accum_output images/mandelbrot_accum.bmp

images/julia.bmp: fractal
	./fractal --width 512 --height 384 --julia_real 0.3 --julia_imag 0.6 --output $@ --accum_output images/julia_accum.bmp

