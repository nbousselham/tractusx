FROM openjdk:11
ADD target/gateway-service-0.0.1.jar gateway-service.jar
EXPOSE 9092
ENTRYPOINT ["java","-jar","gateway-service.jar"]