FROM gradle:8.7.0-jdk21 AS build

WORKDIR /app

COPY *.gradle.kts /app
COPY lombok.config /app
COPY src /app/src

RUN gradle build --no-daemon --project-prop org.gradle.welcome=never

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/ocean-1.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
