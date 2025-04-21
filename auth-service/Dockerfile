FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/auth-service-*-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]