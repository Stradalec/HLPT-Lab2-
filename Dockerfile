FROM eclipse-temurin:11-jre
WORKDIR /app
COPY app-fat.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
