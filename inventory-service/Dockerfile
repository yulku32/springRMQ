FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/inventory-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]