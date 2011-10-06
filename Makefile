LEJOS_HOME=../lejos_nxj
CC=${LEJOS_HOME}/bin/nxjc
SEND=${LEJOS_HOME}/bin/nxj --bluetooth -u
RM=rm -f

TARGETS=Veelhoek.class

all: clean build

build: ${TARGETS}

upload: ${TARGETS}
	@for t in $^; do \
		echo "<-- uploading $$t"; \
		${SEND} `echo $$t | sed -e 's/\.class//'`; \
	done

%.class: %.java
	@echo "*** building ${<:.class=}"
	@${CC} $<

clean:
	@${RM} *.class
