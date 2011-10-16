JAVAC=javac
JAVA=java
RM=rm -f

TARGETS=SimulationRunner.class

all: clean build

build: ${TARGETS}

run: ${TARGETS}
	@${JAVA} ${<:.class=}

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

clean:
	@${RM} *.class
