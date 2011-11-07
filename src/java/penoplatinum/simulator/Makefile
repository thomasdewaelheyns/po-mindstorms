RM=rm -f

TARGETS=SimulationRunner.class
CP=../../

JAVAC=javac -cp ${CP}
JAVA=java -cp ${CP}


all: clean build

build: ${TARGETS}

run: ${TARGETS}
	@${JAVA} penoplatinum.simulator.${<:.class=}

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

test:
	@( cd t; make test )

clean:
	@${RM} *.class
