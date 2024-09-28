# Build stage
FROM ubuntu:latest AS build

# Set the working directory inside the container
WORKDIR /app

# Install dependencies (with updated repository if needed)
RUN apt-get update && \
    apt-get install -y \
    openjdk-21-jdk-headless \
    maven

# Copy project files into the container
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Expose port for the application
EXPOSE 8080

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variable for Docker socket path
ENV DOCKER_SOCKET_PATH=unix:///var/run/docker.sock

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]