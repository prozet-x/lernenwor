FROM gradle:7.4.2-jdk17-alpine as builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/lernenwor-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]