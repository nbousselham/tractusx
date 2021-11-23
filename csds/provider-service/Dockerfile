FROM openjdk:11
ADD target/provider-service-0.0.1.jar provider-service.jar
EXPOSE 8889
ENTRYPOINT ["java","-jar","provider-service.jar"]