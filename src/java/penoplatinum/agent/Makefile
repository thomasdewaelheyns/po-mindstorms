RM=rm -rf

TARGETS=AgentRunner.class
JAR_CLI=../../../../lib/log4j-1.2.16.jar
JAR_LOG4J=../../../../lib/mysql-connector-java-5.0.8-bin.jar
JAR_MYSQL=../../../../lib/commons-cli-1.2.jar
JAR_LEJOS=../../../../../lejos_nxj/lib/pccomm.jar
JAR_BLUE=../../../../../lejos_nxj/3rdparty/lib/bluecove.jar
LIBS=${JAR_CLI}:${JAR_LOG4J}:${JAR_MYSQL}:${JAR_LEJOS}:${JAR_BLUE}
CP=../../:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -d32 -cp ${CP}:${BUILD_DIR}:${LIBS}

MYSQL=/usr/local/mysql/bin/mysql -uplatinum -pplatinum

OPTIONS=

all: ${TARGETS}

db:
	${MYSQL} < schema.sql

run: ${TARGETS} config
	@${JAVA} penoplatinum.agent.${<:.class=} ${OPTIONS}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

config: ${BUILD_DIR}
	@cp log4j.properties ${BUILD_DIR}

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

test:
	@( cd t; make test )

clean:
	@${RM} ${BUILD_DIR} *.class
