TARGETS=penoplatinum/util/RotationTest.test \
		penoplatinum/util/BearingTest.test \
		penoplatinum/util/PointTest.test \
		penoplatinum/util/TransformationTRTTest.test \
		penoplatinum/util/ScannerTest.test \
		penoplatinum/util/Color.test \
		penoplatinum/grid/SectorTest.test

JUNIT_JAR=../lib/junit-4.10.jar
MOCKITO_JAR=../lib/mockito-all-1.9.0.jar
LIBS=${JUNIT_JAR}:${MOCKITO_JAR}

BUILD_DIR=build
CP=.:../src/java:${LIBS}

JAVAC=javac -cp ${CP} -d ${BUILD_DIR} -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}
JUNIT=${JAVA} junit.textui.TestRunner

RM=rm -rf

all: clean ${TARGETS}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

%.test: %.class
	@echo "*** running $(subst /,.,$(<:.class=))"
	@${JUNIT} $(subst /,.,$(<:.class=))

${BUILD_DIR}:
	@mkdir -p build

clean:
	@${RM} ${BUILD_DIR}
