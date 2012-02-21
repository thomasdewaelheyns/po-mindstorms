CC=javac

TARGETS=MazeRunner.class

MAP=test.maze

all: 

chase: MazeRunner.class
	java MazeRunner ${MAP}

discover: Discovery.class
	java Discovery ${MAP}

%.class: %.java
	${CC} $<

clean:
	@rm -f *.class

test:
	@(cd t; make test)
