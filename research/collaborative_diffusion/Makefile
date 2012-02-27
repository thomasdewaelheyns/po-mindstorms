RM=rm -rf

LIBS=:lib/commons-collections-3.2.1.jar
CP=./:${LIBS}

BUILD_DIR=build

JAVAC=javac -cp ${CP} -d build -Xlint:unchecked
JAVA=java -cp ${BUILD_DIR}:${LIBS}

MAP=test.maze

all: 

hunt: GridRunner.class
	${JAVA} GridRunner ${MAP}

discover: Discovery.class
	${JAVA} Discovery ${MAP}

%.class: %.java ${BUILD_DIR}
	@echo "*** building ${<:.class=}"
	@${JAVAC} $<

${BUILD_DIR}:
	@mkdir ${BUILD_DIR}

clean:
	@${RM} ${BUILD_DIR} *.class
  
test:
	@(cd t; make test)
