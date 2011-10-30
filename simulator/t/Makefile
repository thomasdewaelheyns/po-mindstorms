JUNIT_JAR=/usr/share/java/junit.jar
CP=.:../:${JUNIT_JAR}

JAVAC=javac -cp ${CP}
JAVA=java -cp ${CP}
JUNIT=${JAVA} junit.textui.TestRunner

TARGETS=TileTest.test MapTest.test DistanceTest.test

RM=rm -f

all: clean build

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

%.test: %.class
	@${JUNIT} ${<:.class=}

test: ${TARGETS}

clean:
	@${RM} *.class
