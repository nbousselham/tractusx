#
# Copyright (c) 2021-2002 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

# Docker buildfile to containerize the anonymizer
 
# Compile/Build the jar
FROM maven:3-jdk-11 AS maven

ARG MAVEN_OPTS ""

WORKDIR /app

COPY . .

RUN mvn clean install -Dmaven.javadoc.skip=true -DskipTests -q

# Deploy the service
FROM openjdk:11-jre-buster

ARG HTTP_PROXY ""
RUN if [[ -n "${HTTP_PROXY}"  ]]; then echo "Acquire::http::Proxy \"${HTTP_PROXY}\"" >> /etc/apt/apt.conf.d/proxy.conf; fi
RUN if [[ -n "${HTTP_PROXY}" ]]; then echo "Acquire::https::Proxy \"${HTTP_PROXY}\"" >> /etc/apt/apt.conf.d/proxy.conf; fi

RUN apt-get -y upgrade
RUN apt-get -y update
RUN adduser --system --group spring

RUN mkdir -p /anonymizer

RUN chown spring:spring /anonymizer

USER spring:spring

WORKDIR /anonymizer

RUN mkdir persistence

COPY --from=maven /app/anonymizer/target/anonymizer*.jar app.jar

ENV SERVER_PORT 8080

EXPOSE 8080

ENTRYPOINT [ "java","-jar","/anonymizer/app.jar" ]
