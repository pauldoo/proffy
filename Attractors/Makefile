all: out_2_1.zip out_4_1.zip out_8_1.zip out_16_1.zip out_32_1.zip out_64_1.zip out_2_4.zip out_8_4.zip

clean:
	rm -f *.class *.zip

out_2_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 20000 1000 0.08 15 > $@

out_4_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 40000 1000 0.04 15 > $@

out_8_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 80000 1000 0.02 15 > $@
out_2_4.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 20000 4000 0.02 15 > $@

out_16_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 160000 1000 0.01 15 > $@

out_32_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 320000 1000 0.005 15 > $@
out_8_4.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 80000 4000 0.005 15 > $@

out_64_1.zip: Attractors.class
	java -server Attractors 0 249 1600 1200 640000 1000 0.0025 15 > $@

Attractors.class: Attractors.java
	javac -g Attractors.java

