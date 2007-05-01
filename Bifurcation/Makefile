all: test.zip

clean:
	rm -f *.class test.zip

test.zip: Bifurcation.class
	java -server Bifurcation 4000 1000 3 125000 0.012 3.5 4 > $@
	unzip -o test.zip

Bifurcation.class: Bifurcation.java
	javac -g Bifurcation.java

