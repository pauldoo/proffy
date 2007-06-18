all: out.zip

clean:
	rm -f *.class *.zip

out.zip: Cactus.class
	#time java -server Cactus 3500 4950 3 2000 > $@
	time java -server Cactus 4000 4000 3 500 > $@
	unzip -o out.zip

Cactus.class: Cactus.java
	javac -g Cactus.java

