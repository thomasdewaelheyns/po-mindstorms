RM=rm -rf

LIBS=:lib/commons-collections-3.2.1.jar
CP=./:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}

MAP=test.map
SLEEP=25

all: 

show: ShowMap.class
	${JAVA} ${<:.class=} ${MAP}

discover: MiniSimulation.class
	${JAVA} ${<:.class=} ${MAP} ${SLEEP}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

clean:
	@${RM} ${BUILD_DIR} *.class
  
test:
	@(cd t; make test)
