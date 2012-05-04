TARGETS=RunAllTests.test

LIB_DIR=../lib
JAR_JUNIT=${LIB_DIR}/junit-4.10.jar
JAR_MOCKITO=${LIB_DIR}/mockito-all-1.9.0.jar
JAR_LOG4J=${LIB_DIR}/log4j-1.2.16.jar
JAR_MYSQL=${LIB_DIR}/mysql-connector-java-5.0.8-bin.jar
JAR_CLI=${LIB_DIR}/commons-cli-1.2.jar
JAR_IO=${LIB_DIR}/commons-io-1.2.jar
JAR_LEJOS=${LEJOS_DIR}/lib/pccomm.jar
JAR_BLUE=${LEJOS_DIR}/3rdparty/lib/bluecove.jar
JAR_RABBIT=${LIB_DIR}/rabbitmq-client.jar
JAR_LEJOS=${LEJOS_DIR}/lib/pccomm.jar
JAR_BLUE=${LEJOS_DIR}/3rdparty/lib/bluecove.jar

LIBS=${JAR_JUNIT}:${JAR_MOCKITO}:${JAR_LOG4J}:${JAR_MYSQL}:${JAR_CLI}:${JAR_IO}:${JAR_LEJOS}:${JAR_BLUE}:${JAR_RABBIT}

BUILD_DIR=build
CP=.:../src/java:${LIBS}

JAVAC=javac -cp ${CP} -d ${BUILD_DIR} -Xlint:unchecked -Xlint:deprecation
JAVA=java -cp ${BUILD_DIR}:${LIBS}
JUNIT=${JAVA} junit.textui.TestRunner

RM=rm -rf

CLASS_COUNT=$(shell find ../src -name '*.java' | grep -v fullTests | wc -l | head -1)
LINE_COUNT=$(shell find ../src -name '*.java' | grep -v fullTests | xargs wc -l | sort -rn | head -1)
TEST_CLASS_COUNT=$(shell find . -name '*Test.java' | grep -v fullTests | wc -l | head -1)
TEST_LINE_COUNT=$(shell find . -name '*Test.java' | grep -v fullTests | xargs wc -l | sort -rn | head -1)
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
	@cp -r ../resources build/

clean:
	@${RM} ${BUILD_DIR}
