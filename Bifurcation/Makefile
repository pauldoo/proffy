all: test_005.zip test_008.zip test_012.zip

clean:
	rm -f *.class test.zip

test_005.zip: Bifurcation.class
	java -server -Xmx1024m Bifurcation 16000 4000 3 500000 0.005 3.5 4 > $@
	unzip -o $@

test_008.zip: Bifurcation.class
	java -server -Xmx1024m Bifurcation 16000 4000 3 500000 0.008 3.5 4 > $@
	unzip -o $@

test_012.zip: Bifurcation.class
	java -server -Xmx1024m Bifurcation 16000 4000 3 500000 0.012 3.5 4 > $@
	unzip -o $@

Bifurcation.class: Bifurcation.java
	javac -g Bifurcation.java

