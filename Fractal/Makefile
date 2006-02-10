all: build images

build: libFractal.a Bin/Mandelbrot
images: images/mandelbrot.png images/julia.png images/mandelbrot_accum.png images/julia_accum.png

clean:
	+make -C Fractal clean
	+make -C Bin clean
	rm -rf images

libFractal.a:
	+make -C Fractal

Bin/Mandelbrot: libFractal.a
	+make -C Bin
	
images/%_accum.png: images/%.png

images/mandelbrot.png: Bin/Mandelbrot
	mkdir -p images
	./Bin/Mandelbrot --width 512 --height 384 --output $@ --accum_output images/mandelbrot_accum.png

images/julia.png: Bin/Mandelbrot
	mkdir -p images
	./Bin/Mandelbrot --width 512 --height 384 --julia_real 0.3 --julia_imag 0.6 --output $@ --accum_output images/julia_accum.png


