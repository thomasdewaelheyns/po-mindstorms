LIB_DIR=../lib
LEJOS_DIR=../../lejos_nxj

JAR_LOG4J=${LIB_DIR}/log4j-1.2.16.jar
JAR_MYSQL=${LIB_DIR}/mysql-connector-java-5.0.8-bin.jar
JAR_CLI=${LIB_DIR}/commons-cli-1.2.jar
JAR_IO=${LIB_DIR}/commons-io-1.2.jar
JAR_LEJOS=${LEJOS_DIR}/lib/pccomm.jar
JAR_BLUE=${LEJOS_DIR}/3rdparty/lib/bluecove.jar
JAR_RABBIT=${LIB_DIR}/rabbitmq-client.jar
JAR_LEJOS=${LEJOS_DIR}/lib/pccomm.jar
JAR_BLUE=${LEJOS_DIR}/3rdparty/lib/bluecove.jar

LIBS=${JAR_LOG4J}:${JAR_MYSQL}:${JAR_CLI}:${JAR_IO}:${JAR_LEJOS}:${JAR_BLUE}:${JAR_RABBIT}

BUILD_DIR=build
CP=./:../src/java:${LIBS}

JAVAC=javac -cp ${CP} -d ${BUILD_DIR} -Xlint:unchecked -Xlint:deprecation
JAVA=java -d32 -cp .:${BUILD_DIR}:${LIBS}

RM=rm -rf

ARGS=

rash:
	@BUILD_DIR=build/$@
	@mkdir -p ${BUILD_DIR}
	@echo "*** compiling rash..."
	@${JAVAC} RobotAdminShellRunner.java
	@echo "*** running rash..."
	@${JAVA} RobotAdminShellRunner ${ARGS}

spy:
	@BUILD_DIR=build/$@
	@mkdir -p ${BUILD_DIR}
	@echo "*** compiling MQ spy..."
	@${JAVAC} MQSpy.java
	@echo "*** running MQ spy..."
	@${JAVA} MQSpy ${ARGS}

gw:
	@BUILD_DIR=build/$@
	@mkdir -p ${BUILD_DIR}
	@echo "*** compiling Gateway..."
	@${JAVAC} GatewayRunner.java
	@${JAVAC} ../src/java/penoplatinum/bluetooth/IConnection.java
	@${JAVAC} ../src/java/penoplatinum/bluetooth/IPacketReceiver.java
	@${JAVAC} ../src/java/penoplatinum/gateway/UpdatesJSAppender.java
	@echo "*** running Gateway..."
	@${JAVA} GatewayRunner ${ARGS}

sim:
	@echo "!!! not yet ;-)"
	@BUILD_DIR=build/$@
	@mkdir -p ${BUILD_DIR}
	@echo "*** compiling Simulator..."
	@${JAVAC} SimulationRunner.java
	@${JAVAC}	../src/java/penoplatinum/simulator/SimulatedGatewayClient.java
	@${JAVAC}	../src/java/penoplatinum/simulator/ColorLink.java
	@${JAVAC}	../src/java/penoplatinum/driver/ManhattanDriver.java
	@${JAVAC}	../src/java/penoplatinum/robot/GhostRobot.java
	@${JAVAC}	../src/java/penoplatinum/navigator/GhostNavigator.java
	@${JAVAC}	../src/java/penoplatinum/reporter/DashboardReporter.java
	@${JAVAC}	../src/java/penoplatinum/gateway/UpdatesJSAppender.class
	@cp -r ../src/java/penoplatinum/simulator/images ${BUILD_DIR}/penoplatinum/simulator/
	@echo "*** running Simulator..."
	@${JAVA} SimulationRunner ${ARGS}

%.class: src/java/%.java ${BUILD_DIR}
	@${JAVAC} $<

clean:
	@${RM} ${BUILD_DIR}

test:
	@(cd test; make && make stat)

.PHONY:	test
