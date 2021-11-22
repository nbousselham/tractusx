FROM openjdk:11
ADD target/config-0.0.1.jar config.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","config.jar"]
HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:9090/actuator/health || exit 1
