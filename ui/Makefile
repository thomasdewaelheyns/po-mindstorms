JAVAC=javac
JAVA=java
RM=rm -f

TARGETS=UIRunner.class

all: clean build

build: ${TARGETS}

run: ${TARGETS}
	@${JAVA} ${<:.class=}

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

test:
	@( cd t; make test )

clean:
	@${RM} *.class
