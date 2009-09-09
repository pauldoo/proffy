ATISTREAMSDKROOT ?= /home/pauldoo/ati-stream-sdk-v2.0-beta2-lnx64
LD_LIBRARY_PATH := $(LD_LIBRARY_PATH):$(ATISTREAMSDKROOT)/lib/x86_64
export LD_LIBRARY_PATH

output.txt: test WarpOpenCL.cl
	./test | tee $@

test: main.c
	gcc -std=c99 -Wall -O3 -march=native -I$(ATISTREAMSDKROOT)/include -L$(ATISTREAMSDKROOT)/lib/x86_64 -lm -lOpenCL -o $@ $<

clean:
	rm -f output.txt test

