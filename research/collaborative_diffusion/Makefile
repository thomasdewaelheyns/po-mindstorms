CC=javac

TARGETS=MazeRunner.class

MAP=test.maze

all: ${TARGETS}

run: ${TARGETS}
	java MazeRunner ${MAP}

%.class: %.java
	${CC} $<

clean:
	@rm -f *.class

test:
	@(cd t; make test)
