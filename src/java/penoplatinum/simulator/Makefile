RM=rm -rf

TARGETS=SimulationRunner.class
LIBS=:../../../../lib/commons-cli-1.2.jar
CP=../../:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}

OPTIONS=

all: ${TARGETS}

run: ${TARGETS} images
	@${JAVA} penoplatinum.simulator.${<:.class=} ${OPTIONS}

benchmark: BenchmarkRunner.class
	@${JAVA} penoplatinum.simulator.${<:.class=}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

images: ${BUILD_DIR}
	@mkdir -p ${BUILD_DIR}/penoplatinum/simulator/
	@cp -r images ${BUILD_DIR}/penoplatinum/simulator/

test:
	@( cd t; make test )

clean:
	@${RM} ${BUILD_DIR} *.class
