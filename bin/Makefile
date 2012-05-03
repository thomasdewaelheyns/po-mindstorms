LIB_DIR=../lib
JAR_LOG4J=${LIB_DIR}/log4j-1.2.16.jar
JAR_MYSQL=${LIB_DIR}/mysql-connector-java-5.0.8-bin.jar
JAR_CLI=${LIB_DIR}/commons-cli-1.2.jar
JAR_IO=${LIB_DIR}/commons-io-1.2.jar
JAR_LEJOS=${LEJOS_DIR}/lib/pccomm.jar
JAR_BLUE=${LEJOS_DIR}/3rdparty/lib/bluecove.jar
JAR_RABBIT=${LIB_DIR}/rabbitmq-client.jar
LIBS=${JAR_LOG4J}:${JAR_MYSQL}:${JAR_CLI}:${JAR_IO}:${JAR_LEJOS}:${JAR_BLUE}:${JAR_RABBIT}

BUILD_DIR=build/${PROJECT}
CP=../src/java:${LIBS}

JAVAC=javac -cp ${CP} -d ${BUILD_DIR} -Xlint:unchecked -Xlint:deprecation
JAVA=java -cp ${BUILD_DIR}:${LIBS}

RM=rm -rf

RASH_ARGS=-d

rash: project-rash ${BUILD_DIR}
	@echo "*** compiling rash..."
	@${JAVAC} RobotAdminShellRunner.java
	@echo "*** running rash..."
	@${JAVA} RobotAdminShellRunner ${RASH_ARGS}

project-%:
	$(eval PROJECT=rash)

%.class: src/java/%.java ${BUILD_DIR}
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir -p ${BUILD_DIR}

clean:
	@${RM} ${BUILD_DIR}

test:
	@(cd test; make && make stat)

.PHONY:	test
