TARGETS=penoplatinum/grid/SectorTest.test
BUILD=build

JUNIT_JAR=../lib/junit-4.10.jar
CP=.:../src/java:${JUNIT_JAR}

JAVAC=javac -cp ${CP}
JAVA=java -cp ${CP}
JUNIT=${JAVA} junit.textui.TestRunner

RM=rm -rf

all: clean ${BUILD}

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

%.test: %.class
	@echo "*** running $(subst /,.,$(<:.class=))"
	@${JUNIT} $(subst /,.,$(<:.class=))

test: ${TARGETS}

${BUILD}: clean
	mkdir build

clean:
	@${RM} ${BUILD}
