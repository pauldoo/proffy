all: out_2_1.zip out_4_1.zip out_8_1.zip out_16_1.zip out_32_1.zip out_64_1.zip

clean:
	rm -f *.class *.zip

out_2_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 20000 1000 0.08 > $@

out_4_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 40000 1000 0.04 > $@

out_8_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 80000 1000 0.02 > $@

out_16_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 160000 1000 0.01 > $@

out_32_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 320000 1000 0.005 > $@

out_64_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 640000 1000 0.0025 > $@

Attractors.class: Attractors.java
	javac -g Attractors.java

