FROM gradle:7.4.2-jdk17-alpine

EXPOSE 8080

WORKDIR /app

#RUN chmod +x ./start_script.sh

#RUN gradle installDist

RUN gradle clean bootJar

COPY ./build/libs/lernenwor-0.0.1-SNAPSHOT.jar ./app.jar

#CMD ["./start_script.sh"]

CMD ["./start_script.sh"]