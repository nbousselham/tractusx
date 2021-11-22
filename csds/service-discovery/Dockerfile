FROM openjdk:11
ADD target/service-discovery-0.0.1.jar service-discovery.jar
EXPOSE 9091
ENTRYPOINT ["java","-jar","service-discovery.jar"]
