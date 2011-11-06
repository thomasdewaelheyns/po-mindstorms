LEJOS_HOME=../../lejos_nxj
CP=./:../src/Common/src:./src/Common/test:../src/Client/src:../src/Robot/src
CC=${LEJOS_HOME}/bin/nxjc -cp ${CP}
NAME=NXT
SEND=${LEJOS_HOME}/bin/nxj -cp ${CP} -b -n ${NAME}
RUN=${SEND} -r
RM=rm -f
PROJECT=demo2

TARGETS=${PROJECT}.class

CLASSNAMES=`echo ${TARGETS} | sed -e 's/\.class//g'`;

all: clean build

build: ${TARGETS}

upload: ${TARGETS}
	@echo "<-- uploading ${<:.class=}";
	@${SEND} ${CLASSNAMES}

run: ${TARGETS}
	@echo "<-- uploading and running ${<:.class=}";
	@${RUN} ${CLASSNAMES}

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${CC} $<

clean:
	@${RM} *.class
