FROM openjdk:8-jdk-alpine AS build
WORKDIR /app
# Kopiere Inhalt aus person-scs in Docker-Container
COPY . ./
RUN ./gradlew --no-daemon --stacktrace clean bootJar



FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
WORKDIR /app

COPY wait-for-it.sh .
COPY --from=build /app/build/libs/*.jar app.jar

CMD java -jar app.jar
