RM=rm -f
PROJECT=demo2
TARGETS=${PROJECT}.class
CP=./:../src/java:../src/lejos

JAVAC=javac -cp ${CP}
JAVA=java -cp ${CP}

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
