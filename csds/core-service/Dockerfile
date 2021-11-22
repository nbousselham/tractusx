FROM openjdk:11
ADD target/core-service-0.0.1.jar core-service.jar
EXPOSE 9094
ENTRYPOINT ["java","-jar","core-service.jar"]