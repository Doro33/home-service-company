# ---- Build Stage ----
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Let Docker know the port (will be used in docker-compose)
# But don't hardcode it here; .env will handle mapping
# EXPOSE is just documentation — no runtime binding
ARG APP_PORT
ENV APP_PORT=${APP_PORT}
EXPOSE ${APP_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]

