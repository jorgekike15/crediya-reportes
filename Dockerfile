FROM amazoncorretto:21.0.8-alpine
WORKDIR /app
COPY applications/app-service/build/libs/crediya-reportes.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]