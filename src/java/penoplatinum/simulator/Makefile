RM=rm -rf

TARGETS=SimulationRunner.class \
				SimulatedGatewayClient.class \
				ColorLink.class \
			  ../driver/GhostDriver.class \
			  ../pacman/GhostRobot.class \
				../pacman/GhostNavigator.class \
				../pacman/DashboardReporter.class \
				../gateway/UpdatesJSAppender.class
LIB_DIR=../../../../lib
LIB_CLI=${LIB_DIR}/commons-cli-1.2.jar
LIB_COLL=${LIB_DIR}/commons-collections-3.2.1.jar
LIB_IO=${LIB_DIR}/commons-io-1.2.jar
LIB_MQ=${LIB_DIR}/rabbitmq-client.jar
LIB_LOG4J=${LIB_DIR}/log4j-1.2.16.jar
LIB_MYSQL=${LIB_DIR}/mysql-connector-java-5.0.8-bin.jar
LIBS=${LIB_COLL}:${LIB_IO}:${LIB_CLI}:${LIB_MQ}:${LIB_LOG4J}:${LIB_MYSQL}
CP=../../:${LIBS}

LOG4J_PROPERTIES=../../log4j.properties

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}

OPTIONS=-m ../../../../maps/wolfraam.txt -s 2

all: ${TARGETS}

run: ${TARGETS} files
	${JAVA} penoplatinum.simulator.${<:.class=} ${OPTIONS}

benchmark: BenchmarkRunner.class
	@${JAVA} penoplatinum.simulator.${<:.class=}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

files: ${BUILD_DIR}
	@mkdir -p ${BUILD_DIR}/penoplatinum/simulator/
	@cp -r images ${BUILD_DIR}/penoplatinum/simulator/
	@cp ${LOG4J_PROPERTIES} ${BUILD_DIR}

test:
	@( cd t; make test )

clean:
	@${RM} ${BUILD_DIR} *.class
