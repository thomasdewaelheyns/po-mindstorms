RM=rm -rf

LIB_DIR=../../../../lib
LEJOS_DIR=../../../../../lejos_nxj

TARGETS=GatewayRunner.class UpdatesJSAppender.class
JAR_LOG4J=${LIB_DIR}/log4j-1.2.16.jar
JAR_MYSQL=${LIB_DIR}/mysql-connector-java-5.0.8-bin.jar
JAR_CLI=${LIB_DIR}/commons-cli-1.2.jar
JAR_IO=${LIB_DIR}/commons-io-1.2.jar
JAR_LEJOS=${LEJOS_DIR}/lib/pccomm.jar
JAR_BLUE=${LEJOS_DIR}/3rdparty/lib/bluecove.jar
JAR_RABBIT=${LIB_DIR}/rabbitmq-client.jar
LIBS=${JAR_LOG4J}:${JAR_MYSQL}:${JAR_CLI}:${JAR_IO}:${JAR_LEJOS}:${JAR_BLUE}:${JAR_RABBIT}
CP=.:../../:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -d32 -cp ${CP}:${BUILD_DIR}:${LIBS}

MYSQL=/usr/local/mysql/bin/mysql -uplatinum -pplatinum
MYSQLDUMP=/usr/local/mysql/bin/mysqldump -uplatinum -pplatinum

OPTIONS=

LOG4J_PROPERTIES=../../log4j.properties

# argument that can be passed to the run target to setup different "ghost"
# possible names are: red, pink, cyan and orange
ME=pink

all: ${TARGETS}

db:
	${MYSQL} < schema.sql

run: ${TARGETS} files
	@${JAVA} penoplatinum.gateway.${<:.class=} ${OPTIONS}

run-mq: MQRunner.class
	@${JAVA} penoplatinum.gateway.MQRunner ${ME}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

dashboard: test.class ../Utils.class
	${JAVA} penoplatinum.gateway.test

files: ${BUILD_DIR}
	@cp ${LOG4J_PROPERTIES} ${BUILD_DIR}

backup:
	  ${MYSQLDUMP} robots > demo1.sql

test:
	@( cd t; make test )

clean:
	@${RM} ${BUILD_DIR} *.class
