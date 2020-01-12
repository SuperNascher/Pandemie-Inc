# Multi-Stage-Build
# 1. First Step - Build the server software
FROM maven:3-jdk-14
ADD . /pandemieinc
WORKDIR /pandemieinc
RUN mvn clean install

# 2. Second Step - Run the server software
FROM openjdk:14-jdk
MAINTAINER Kevin Rohland
VOLUME /tmp
COPY --from=0 "/pandemieinc/target/pandemieinc-*-RELEASE.jar" pandemieinc.jar
EXPOSE 8080
CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /pandemieinc.jar" ]
