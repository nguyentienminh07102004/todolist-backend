# Stage 1: build
# Start with a Maven image that includes JDK 17
FROM maven:3.9.6-amazoncorretto-17-debian AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

#Stage 2: create image
# Start with Amazon Correto JDK 17
FROM amazoncorretto:17.0.12

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.war app.war

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.war"]