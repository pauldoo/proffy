objects := $(patsubst %.cpp, %.o, $(wildcard *.cpp))
deps := $(patsubst %.cpp, %.dep, $(wildcard *.cpp))

CXXFLAGS = -Wall -O2 -g -pipe
CPPFLAGS =
LIBS = -lboost_program_options -lcrypto++

all: External.h.gch lrc

clean:
	rm -f lrc *.o *.gch

lrc: $(objects)
	$(CXX) $(CXXFLAGS) -o $@ $(objects) $(LIBS)

%.dep: %.cpp
	$(CXX) -MM $< > $@ $(CPPFLAGS)

%.h.gch: %.h
	$(CXX) $(CXXFLAGS) -c -o $@ $< $(CPPFLAGS)

%.o: %.cpp %.dep
	$(CXX) $(CXXFLAGS) -c -o $@ $< $(CPPFLAGS)

include $(deps)

