RM=rm -rf

TARGETS=SimulationRunner.class \
			  ../../penoplatinum/pacman/GhostRobot.class \
			  ../../penoplatinum/driver/GhostDriver.class \
				../../penoplatinum/pacman/GhostNavigator.class \
				../../penoplatinum/simulator/SimulationRobotAgent.class \
				../../penoplatinum/simulator/ColorLink.class
LIB_DIR=../../../../lib
LIB_CLI=${LIB_DIR}/commons-cli-1.2.jar
LIB_COLL=${LIB_DIR}/commons-collections-3.2.1.jar
LIB_MQ=${LIB_DIR}/rabbitmq-client.jar
LIBS=${LIB_COLL}:${LIB_CLI}:${LIB_MQ}
CP=../../:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}

OPTIONS=

all: ${TARGETS}

run: ${TARGETS} copy_images
	@${JAVA} penoplatinum.simulator.${<:.class=} ${OPTIONS}

benchmark: BenchmarkRunner.class
	@${JAVA} penoplatinum.simulator.${<:.class=}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

copy_images: ${BUILD_DIR}
	@mkdir -p ${BUILD_DIR}/penoplatinum/simulator/
	@cp -r images ${BUILD_DIR}/penoplatinum/simulator/

test:
	@( cd t; make test )

clean:
	@${RM} ${BUILD_DIR} *.class
