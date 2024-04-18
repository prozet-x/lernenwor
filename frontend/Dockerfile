FROM node:lts-alpine3.19

EXPOSE 3000

WORKDIR /app

COPY . .

CMD ["./start_script.sh"]





#ENV PATH /app/node_modules/.bin:$PATH

#COPY package.json ./
#COPY package-lock.json ./
RUN npm install --silent
#RUN npm install react-scripts@3.4.1 -g --silent

# add app
#COPY . ./

# start app
CMD ["npm", "start"]