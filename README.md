# ChatWithRedis

A real-time chat application built with **Java 21, Spring Boot, MySQL, Redis, WebSocket/STOMP, and React + TypeScript** with a glassmorphism UI.

## Features in this starter
- Username/password registration and login with BCrypt password hashing and JWT access tokens
- Real-time chat using Spring WebSocket + STOMP
- Durable message history stored in MySQL
- Redis Pub/Sub to relay messages between backend instances
- Redis TTL-based online presence
- React glassmorphism interface with connection state and online-user list
- Docker Compose for local MySQL and Redis

> This is an initial learning/MVP implementation, not a fully hardened production chat platform. Before public deployment, add conversation membership/authorization, rate limits, token revocation/refresh, migrations, observability, and a security review. The starter UI uses a shared `general` room.

## Requirements
- Java 21
- Maven 3.9+
- Node.js 20+
- Docker Desktop

## 1. Start MySQL and Redis
At repository root, copy `.env.example` to `.env`, replace the example passwords and JWT secret, then run:

```bash
docker compose up -d
```

Compose maps MySQL to host port **3307** and Redis to **6381** to avoid collisions with common local defaults. The `.env` file configures Docker Compose. For Spring Boot, set environment variables in your terminal/STS Run Configuration if you change the defaults.

## 2. Run backend
Open a terminal:

```bash
cd backend
mvn spring-boot:run
```

API base URL: `http://localhost:8080/api`; WebSocket endpoint: `http://localhost:8080/ws`.

Backend environment variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `REDIS_HOST`, `REDIS_PORT`, `JWT_SECRET`. Defaults match the local Compose credentials. Change secrets before use beyond local development.

## 3. Run frontend
Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

Optional `frontend/.env`:

```properties
VITE_API_BASE_URL=http://localhost:8080/api
VITE_WS_URL=http://localhost:8080/ws
```

## 4. Try the app
1. Create an account with a username of 3–30 letters/numbers/underscore/dot/hyphen and a password of at least 8 characters.
2. Open another browser/incognito session and create a second account.
3. Send messages from either session in General chat.
4. Presence expires after 60 seconds and the UI refreshes the heartbeat every 25 seconds.

## API overview
- `POST /api/auth/register` — register
- `POST /api/auth/login` — login
- `GET /api/chat/{conversationId}` — latest 100 messages (Bearer token required)
- `POST /api/presence/heartbeat` — renew online presence (Bearer token required)
- `GET /api/presence/online` — online users (Bearer token required)
- WebSocket `/ws`; STOMP send destination `/app/chat.send`; subscribe to `/topic/conversations/general`. Send JSON: `{"conversationId":"general","content":"Hello!"}`. Include STOMP CONNECT native header `Authorization: Bearer <token>`.

## Architecture notes
- **MySQL** is the durable source of truth for users and messages.
- **Redis TTL** stores temporary presence; it is not the permanent message store.
- **Redis Pub/Sub** relays persisted message events to WebSocket subscribers across backend instances. Pub/Sub is not durable; message history is in MySQL.
- Spring WebSocket/STOMP was selected instead of Socket.IO to keep the realtime server native to Spring Boot.

## Stop local infrastructure
```bash
docker compose down
```
Data remains in named volumes. `docker compose down -v` deletes those volumes and permanently removes local database/cache data; use only if you intentionally want a reset.
