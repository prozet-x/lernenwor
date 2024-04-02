FROM gradle:7.4.2-jdk18-alpine

EXPOSE 8080

WORKDIR /app

COPY . .

RUN gradle installDist

CMD ["gradle", "run"]