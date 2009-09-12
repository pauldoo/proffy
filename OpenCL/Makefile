ATISTREAMSDKROOT ?= /home/pauldoo/ati-stream-sdk-v2.0-beta2-lnx64
LD_LIBRARY_PATH := $(LD_LIBRARY_PATH):$(ATISTREAMSDKROOT)/lib/x86_64
export LD_LIBRARY_PATH

OBJS := main.o common.o warpvanilla.o warpopencl.o warpvanilla2.o
CFLAGS := -std=c99 -Wall -O3 --fast-math -march=native -I$(ATISTREAMSDKROOT)/include

output.txt: test WarpOpenCL.cl
	./test | tee $@

test: $(OBJS)
	gcc -Wall -L$(ATISTREAMSDKROOT)/lib/x86_64 -lm -lOpenCL -o $@ $(OBJS)

-include $(OBJS:.o=.d)

%.o: %.c
	gcc -c $(CFLAGS) $*.c -o $*.o
	gcc -MM $(CFLAGS) $*.c > $*.d

clean:
	rm -f output.txt test *.o *.d

