# Use official Java image
FROM openjdk:17-jdk-slim

# Set working dir
WORKDIR /app

# Copy Maven build output
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run app with active profile
ENTRYPOINT ["java", "-jar", "app.jar"]