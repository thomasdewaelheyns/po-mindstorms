PROJECT=platinum

DIR=$(shell pwd)

JAVAC=javac
JAR=jar
JAR_MAKE=${JAR} -cf

JBOSS=jboss-4.2.3.GA
JBOSS_CONFIG=default
JBOSS_INSTANCE=${JBOSS}/server/${JBOSS_CONFIG}
JBOSS_DEPLOY=${JBOSS_INSTANCE}/deploy

WAR=${PROJECT}.war
DS=db-ds.xml

SRCS=*.java
BUILD_DIR=build

LIB_SERVLET=${JBOSS_INSTANCE}/lib/servlet-api.jar

all: ${WAR}

${WAR}: compile-war
	@echo "*** packaging $@"
	@(cd ${BUILD_DIR}; ${JAR_MAKE} ../${WAR} *)

compile-war: ${SRCS} ${BUILD_DIR}
	@echo "*** compiling WAR"
	@mkdir -p ${BUILD_DIR}/WEB-INF/classes
	@cp web.xml ${BUILD_DIR}/WEB-INF
	@${JAVAC} -classpath ${LIB_SERVLET} -d ${BUILD_DIR}/WEB-INF/classes ${SRCS}

${BUILD_DIR}:
	@mkdir -p ${BUILD_DIR}

deploy: ${DS} ${WAR}
	@echo "*** deploying datasource db"
	@cp ${DS} ${JBOSS_DEPLOY}/
	@echo "*** deploying ${WAR}"
	@cp ${WAR} ${JBOSS_DEPLOY}/

undeploy:
	@echo "*** undeploying ${WAR}"
	@rm ${JBOSS_DEPLOY}/${WAR}
	@echo "*** undeploying datasource db"
	@rm ${JBOSS_DEPLOY}/${DS}

clean:
	@rm -rf ${BUILD_DIR}
	@rm -f ${WAR}
