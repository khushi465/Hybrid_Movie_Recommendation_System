# Use Java runtime
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Expose backend port
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]