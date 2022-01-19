FROM openjdk:11
ADD target/consumer-service-0.0.1.jar consumer-service.jar

RUN echo -n | openssl s_client -connect 20.79.226.28:8080 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ~/20.79.226.28.crt

RUN keytool -import -v -trustcacerts -noprompt -alias 20.79.226.28 -file ~/20.79.226.28.crt -keystore $JAVA_HOME/lib/security/cacerts -keypass changeit -storepass changeit

EXPOSE 9095
ENTRYPOINT ["java","-jar","consumer-service.jar"]
