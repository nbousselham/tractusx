FROM openjdk:11
ADD target/documentation-service-0.0.1.jar documentation-service.jar
EXPOSE 9093
ENTRYPOINT ["java","-jar","documentation-service.jar"]