all:
	javac -Xlint:all -g *.java
	java -ea -server -Xmx512m Forward source.png normal.bmp reordered.bmp
	java -ea -server -Xmx512m Reverse reordered.bmp reconstruction.bmp

clean:
	rm -f *.class normal.bmp reordered.bmp reconstruction.bmp

