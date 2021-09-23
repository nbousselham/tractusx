# Dependencies
FROM maven:3-jdk-11 AS maven
WORKDIR /build
COPY pom.xml .
COPY src src
# the --mount option requires BuildKit.
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package

# Copy the jar and build image
FROM adoptopenjdk:11-jre-hotspot AS runtime

WORKDIR /app

COPY --from=maven /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
