FROM node:12.13-alpine

RUN mkdir /app
WORKDIR /app
COPY . /app
ENTRYPOINT npm i && npm run start

EXPOSE 3000
