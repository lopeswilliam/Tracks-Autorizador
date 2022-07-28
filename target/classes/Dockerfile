FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mini-autorizador-0.0.1.jar
ENTRYPOINT ["java","-jar","/mini-autorizador-0.0.1.jar"]