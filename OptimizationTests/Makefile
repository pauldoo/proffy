all: Minimum.class Minimum Minimum.s Convolve

clean:
	rm -f *.class *.s *.o Minimum Convolve

%.o: %.cxx
	g++ -Wall -g -O3 -c -S -o $@.s $^
	g++ -Wall -g -O3 -c -o $@ $^

Minimum.class: Minimum.java
	javac -Xlint:all -g $^
	#java -server -Xmx512m Minimum

Minimum.s: Minimum.cxx
	g++ -Wall -g -O3 -c -S -o $@ $^

Minimum: Minimum.cxx
	g++ -Wall -g -O3 -o $@ $^
	#./Minimum

Convolve: Convolve.o Convolve_Intrinsics.o Convolve_Vanilla.o
	g++ -Wall -g -O3 -o $@ $^
	./Convolve

