# ChatWithRedis

A real-time chat application built with Spring Boot, MySQL, Redis, and React + TypeScript.

## Planned architecture
- Spring Boot REST API and STOMP over WebSocket for real-time messaging
- MySQL for durable users and message history
- Redis for online presence (TTL) and cross-instance Pub/Sub
- React + TypeScript glassmorphism interface
- Docker Compose for local infrastructure

## Status
Initial repository bootstrap. Setup and run instructions will be expanded as implementation lands.

## Security notes
Use environment variables for secrets and database credentials. Do not commit production secrets.
