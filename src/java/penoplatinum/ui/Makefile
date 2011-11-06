RM=rm -f

TARGETS=UIRunner.class
CP=../../:../../../../../lejos_nxj/lib/pccomm.jar

JAVAC=javac -cp ${CP}
JAVA=java -cp ${CP}

all: clean build

build: ${TARGETS}

run: ${TARGETS}
	@${JAVA} penoplatinum.ui.${<:.class=}

%.class: %.java
	@echo "*** building ${<:.class=}"
	${JAVAC} $<

test:
	@( cd t; make test )

clean:
	@${RM} *.class
