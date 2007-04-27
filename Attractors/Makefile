all: out_2_2.png out_2_4.png out_4_2.png out_4_4.png

clean:
	rm -f *.class *.png

out_2_2.png: Attractors.class
	java -server Attractors 1 1600 1200 20000 2000 0.04 > $@

out_2_4.png: Attractors.class
	java -server Attractors 1 1600 1200 20000 4000 0.02 > $@

out_4_2.png: Attractors.class
	java -server Attractors 1 1600 1200 40000 2000 0.02 > $@

out_4_4.png: Attractors.class
	java -server Attractors 1 1600 1200 40000 4000 0.01 > $@

Attractors.class: Attractors.java
	javac -g Attractors.java

