# Java Spring pet project

## Description
Chat with file upload, built on microservices, uses MySQL and RabbitMQ. Deployed using docker-compose.
- Uses STOMP over WebSockets.
- Users can connect to different chat-signal-service replicas, but still able to communicate with each other.
- Replicas communicate using RabbitMQ.
- File attachments are processed using file service.

## Running locally

Build jars
```
cd scripts
./build-jars.sh
```

Build frontend

> NOTE: requires npm

```
cd frontend
npm i && npm run build
```

Run docker-compose
```
cd deploy
docker-compose up
```

Application will be available at http://localhost:3000

> NOTE: it will take about 1 minute for chat-signals to be available on the frontend due to  Eureka's server workflow.

## More detailed description
List of services:

| Name | Description |
|------|-------------|
| frontend | Client's server. Built using React.js
| file-service | Service responsible for files processing: accepting, storing, serving. Signals about files' processing statuses via RabbitMQ
| chat-signal-service | Provides socket connections for the chat. Persists messages in the database and notifies clients about any updates related to the chat (users connect/disconnect, new messages,  uploaded files' statuses updates). Can be replicated, replicas communicate with each other via RabbitMQ
| discovery-server | Eureka server. Used for dynamic resolution of chat-signal-services' ports
| discovery-api | Provides api to get chat-signal-services' ports
| mySQL | Persists chat messages, file records, connected users
| rabbitMQ | Used for communication between file-service and chat-signal-service and between the latter's replicas
| common | package for common POJOs between services

### RabbitMQ diagram:
![basic](assets/rabbitmq-diagram.png)

### Why use message broker at all?
Mostly to satisfy the quirky requirement I had for this project: users within 1 chatroom could be connected to different services, but still should be able to communicate as they would using a single service. Even though this case is nearly unrealistic in commercial projects.
