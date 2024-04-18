FROM gradle:8.7.0-jdk21 AS build

WORKDIR /app

COPY build.gradle.kts /app
COPY src /app/src

RUN gradle build

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/ocean-1.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
