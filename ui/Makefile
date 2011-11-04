RM=rm -f

TARGETS=UIRunner.class
CP=./:../Common/src:../Common/test:../Client/src

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
