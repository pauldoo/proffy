all: Minimum.class Minimum Minimum_ngcc

Minimum.class: Minimum.java
	javac -Xlint:all -g Minimum.java
	java -server -Xmx512m Minimum

Minimum Minimum.s: Minimum.cxx
	g++ -Wall -g -O3 -c -S -o Minimum.s Minimum.cxx
	g++ -Wall -g -O3 -o Minimum Minimum.cxx
	./Minimum

