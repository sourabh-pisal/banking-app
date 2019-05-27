FROM openjdk:8-jdk-alpine
MAINTAINER pisal.sourabh@gmail.com
VOLUME /tmp
COPY /build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]