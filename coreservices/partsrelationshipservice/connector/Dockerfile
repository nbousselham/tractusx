FROM maven:3-jdk-11 AS maven

RUN git clone https://github.com/eclipse-dataspaceconnector/DataSpaceConnector.git
WORKDIR DataSpaceConnector
RUN --mount=type=cache,target=/root/.gradle --mount=type=cache,target=/root/.m2 ./gradlew publishToMavenLocal -x test

WORKDIR /build
COPY pom.xml pom.xml
COPY prs-connector-consumer prs-connector-consumer
COPY prs-connector-parent prs-connector-parent
COPY prs-connector-provider prs-connector-provider

# the --mount option requires BuildKit.
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package

FROM adoptopenjdk:11-jre-hotspot AS runtime

WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM runtime AS provider
COPY --from=maven /build/prs-connector-provider/target/prs-connector-*.jar app.jar
COPY --from=maven /build/prs-connector-provider/dataspaceconnector-configuration.properties .

FROM runtime AS consumer
COPY --from=maven /build/prs-connector-consumer/target/prs-connector-*.jar app.jar
COPY --from=maven /build/prs-connector-consumer/dataspaceconnector-configuration.properties .
