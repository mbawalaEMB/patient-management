# ------------------------------------------- BUILD stage -------------------------------------------
# Building the application
FROM maven:3.9.11-eclipse-temurin-21 AS builder

# Creating a directory in the container, where the application will reside
WORKDIR /app

# Copying the pom xml to the working directory
COPY pom.xml .

# Cache dependencies if there are no changes, or download them if there were changes
# It forces Maven to download all dependencies, plugins, and parent POMs your project might need for compilation, testing, and packaging.
RUN mvn dependency:go-offline -B

COPY src ./src
# RUN ls -l /app/src
# RUN ls -l /app/pom.xml
# Package the application
RUN mvn clean package -DskipTests

# ------------------------------------------- BUILD stage -------------------------------------------
# Running the app
FROM openjdk:21-jdk AS runner

WORKDIR /app

COPY --from=builder ./app/target/billing-service-*.jar ./app.jar

# REST
EXPOSE 4001

# gRPC
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]