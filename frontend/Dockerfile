FROM node:lts-alpine3.19

EXPOSE 3000

WORKDIR /app

COPY ./package.json .
COPY ./package-lock.json .

RUN npm install --silent

CMD ["npm", "start"]