FROM gradle:7.4.2-jdk17-alpine

EXPOSE 8080

WORKDIR /app

COPY . .

RUN chmod +x ./start_script.sh

RUN gradle installDist

CMD ["./start_script.sh"]