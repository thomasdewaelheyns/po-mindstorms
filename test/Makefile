TARGETS=RunAllTests.test
JUNIT_JAR=../lib/junit-4.10.jar
MOCKITO_JAR=../lib/mockito-all-1.9.0.jar
LIBS=${JUNIT_JAR}:${MOCKITO_JAR}

BUILD_DIR=build
CP=.:../src/java:${LIBS}

JAVAC=javac -cp ${CP} -d ${BUILD_DIR} -Xlint:unchecked -Xlint:deprecation
JAVA=java -cp ${BUILD_DIR}:${LIBS}
JUNIT=${JAVA} junit.textui.TestRunner

RM=rm -rf

CLASS_COUNT=$(shell find ../src -name '*.java' | wc -l | head -1)
LINE_COUNT=$(shell find ../src -name '*.java' | xargs wc -l | sort -rn | head -1)
TEST_CLASS_COUNT=$(shell find . -name '*.java' | wc -l | head -1)
TEST_LINE_COUNT=$(shell find . -name '*.java' | xargs wc -l | sort -rn | head -1)
CLASS_COVERAGE_PCT=$(shell echo "scale=2; ${TEST_CLASS_COUNT}*100/${CLASS_COUNT}" | bc)

all: clean ${TARGETS}

stat:
	@echo "source classes: ${CLASS_COUNT} / ${LINE_COUNT}"
	@echo "test   classes: ${TEST_CLASS_COUNT} / ${TEST_LINE_COUNT}"
	@echo "class coverage: ${CLASS_COVERAGE_PCT}%"


%.class: %.java ${BUILD_DIR}
	@${JAVAC} $<

%.test: %.class
	@echo "*** $(subst /,.,$(<:.class=))"
	@${JUNIT} $(subst /,.,$(<:.class=))

${BUILD_DIR}:
	@mkdir -p build

clean:
	@${RM} ${BUILD_DIR}
