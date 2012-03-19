RM=rm -rf

LIB_DIR=../../../../../lib
LIB_COL=${LIB_DIR}/commons-collections-3.2.1.jar
LIB_MQ=${LIB_DIR}/rabbitmq-client.jar
LIBS=${LIB_COL}:${LIB_MQ}
CP=../../../:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}

MAP=test.map
SHOW_OPTS=
SLEEP=25

PKG=penoplatinum.simulator.mini

all: 

show: ShowMap.class
	${JAVA} ${PKG}.${<:.class=} ${MAP} ${SHOW_OPTS}

discover: MiniSimulation.class
	${JAVA} ${PKG}.${<:.class=} ${MAP} ${SLEEP}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

clean:
	@${RM} ${BUILD_DIR} *.class
  
test:
	@(cd t; make test)
