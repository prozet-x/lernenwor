FROM gradle:7.4.2-jdk17-alpine

EXPOSE 8080

WORKDIR /app

COPY . .

RUN gradle installDist

RUN gradle diffChangelog

CMD ["gradle", "run"]