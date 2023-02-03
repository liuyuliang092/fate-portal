FROM anapsix/alpine-java:8_server-jre_unlimited

ARG PREFIX_NAME=fate-portal
ARG APP_NAME=web

RUN apk update \
    && apk add tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /data

COPY ./target/*.jar ${PREFIX_NAME}-${APP_NAME}.jar
COPY ./src/main/resources/application-prod.properties application.properties

ENV PREFIX_NAME=${PREFIX_NAME} APP_NAME=${APP_NAME} LANG=en_US.UTF-8 TERM=xterm MS=4g NS=2g

ENV MXX="-XX:MaxMetaspaceSize=380m -XX:+PrintGC -Xloggc:./gc_log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./java.hprof -XX:ErrorFile=./java_error.log -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -XX:+UseConcMarkSweepGC -XX:CMSMaxAbortablePrecleanTime=5000 -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses"

ENTRYPOINT ["sh","-c","java -Xms${MS} -Xmx${MS} -XX:NewSize=${NS} -XX:MaxNewSize=${NS} ${MXX} -jar ${PREFIX_NAME}-${APP_NAME}.jar","--spring.config.location=/data/application.properties"]
