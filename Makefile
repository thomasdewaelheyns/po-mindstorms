LEJOS_HOME=../lejos_nxj
CC=${LEJOS_HOME}/bin/nxjc
NAME=NXT
SEND=${LEJOS_HOME}/bin/nxj -b -n ${NAME}
RUN=${SEND} -r
RM=rm -f

TARGETS=Veelhoek.class

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
