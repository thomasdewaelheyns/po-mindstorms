CC=javac

TARGETS=MazeRunner.class

all: ${TARGETS}

run: ${TARGETS}
	java MazeRunner

%.class: %.java
	${CC} $<

clean:
	@rm -f *.class

test:
	@(cd t; make test)
