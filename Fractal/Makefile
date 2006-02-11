all: build

build:
	+make -C Fractal
	+make -C Bin
	mkdir -p images

images: images/mandelbrot.bmp images/julia.bmp images/mandelbrot_accum.bmp images/julia_accum.bmp

clean:
	+make -C Fractal clean
	+make -C Bin clean
	rm -rf images

images/%_accum.bmp: images/%.bmp

images/mandelbrot.bmp: Bin/Mandelbrot
	mkdir -p images
	./Bin/Mandelbrot --width 512 --height 384 --output $@ --accum_output images/mandelbrot_accum.bmp

images/julia.bmp: Bin/Mandelbrot
	mkdir -p images
	./Bin/Mandelbrot --width 512 --height 384 --julia_real 0.3 --julia_imag 0.6 --output $@ --accum_output images/julia_accum.bmp


