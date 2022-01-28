FROM openjdk:11

ADD target/consumer-service-0.0.1.jar consumer-service.jar

RUN echo -n | openssl s_client -connect 20.113.42.10:8181 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ~/20.113.42.10.crt

RUN keytool -import -v -trustcacerts -noprompt -alias 20.113.42.10 -file ~/20.113.42.10.crt -keystore $JAVA_HOME/lib/security/cacerts -keypass changeit -storepass changeit

RUN echo -n | openssl s_client -connect 20.79.215.77:8282 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ~/20.79.215.77.crt

RUN keytool -import -v -trustcacerts -noprompt -alias 20.79.215.77 -file ~/20.79.215.77.crt -keystore $JAVA_HOME/lib/security/cacerts -keypass changeit -storepass changeit

EXPOSE 9096

ENTRYPOINT ["java","-jar","consumer-service.jar"]
