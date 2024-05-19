FROM gradle:7.4.2-jdk17-alpine as builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/lernenwor-0.0.1-SNAPSHOT.jar /app/app.jar
COPY ./entrypoint_prod.sh /app/entrypoint_prod.sh
EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]