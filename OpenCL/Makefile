output.txt: test
	./test | tee $@

test: main.c
	gcc -Wall -Werror -O3 -march=native -lm -o $@ $<

clean:
	rm -f output.txt test

