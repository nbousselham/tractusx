FROM ubuntu:bionic

RUN apt-get update \
    && apt-get -y install default-jdk \
    && apt-get -y install maven

COPY . /scheduler

ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64/

RUN export JAVA_HOME

WORKDIR /scheduler

RUN mvn clean install -Dmaven.test.skip=true

WORKDIR target

RUN mv scheduler-0.0.1-SNAPSHOT.jar scheduler.jar

ENTRYPOINT ["java","-jar","scheduler.jar"]

EXPOSE 8889
