# Use a lightweight OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven build output
COPY target/AngkorLeving_Backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render injects PORT)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app/app.jar"]