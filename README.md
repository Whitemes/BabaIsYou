# Baba Is You - Engineering Edition

A professional reimplementation of the "Baba Is You" puzzle game engine, focusing on **Software Architecture**, **Production Debugging**, and **Cloud-Native Deployment**.

![Status](https://img.shields.io/badge/Status-Production-success)
![Stack](https://img.shields.io/badge/Tech-Java%2021%20%7C%20Spring%20Boot%203%20%7C%20WebSockets%20%7C%20Docker-blue)
![Deployment](https://img.shields.io/badge/Deploy-Railway-blueviolet)

> **🎮 [Play Live on Railway](https://babaisyou-production.up.railway.app)**

## 🚀 Project Overview

This project transforms a legacy academic codebase into a **production-ready, cloud-deployed web application**.
It demonstrates **hexagonal architecture**, **event-driven design**, **WebSocket real-time communication**, and **containerized deployment**.

**Key Achievement:** Diagnosed and resolved critical production bugs (black screen issue) in Railway cloud environment through systematic debugging, logging infrastructure, and cloud-native configuration.

---

## 🏗️ Architecture

### High-Level Design
```mermaid
graph TD
    Browser[Web Browser] -->|HTTPS/WSS| Railway[Railway Cloud Platform]
    Railway -->|Port $PORT| SpringBoot[Spring Boot App]
    SpringBoot -->|WebSocket /game-ws| WSHandler[GameWebSocketHandler]
    WSHandler -->|JSON Grid State| Browser
    Browser -->|Arrow Keys, R, Z| WSHandler
    WSHandler --> Game[Game Controller]
    Game --> Rules[Rules Engine]
    Game --> Level[Level Manager]
    Rules --> Transmutation[Transmutation System]
    Level --> Grid[Grid/Cellule/Element]
```

### Key Engineering Features

#### 🎯 Hexagonal-like Architecture
- **Core game logic** (`model`) completely decoupled from rendering layer (`web`)
- Pure domain logic with zero UI dependencies (AWT/Swing removed)
- **Renderer interface** allows pluggable output (WebSocket, console, file, etc.)

#### ⚡ Event-Driven Real-Time Architecture
- Game state evolves via `GameAction` events (MOVE_UP, MOVE_DOWN, RESTART, etc.)
- Bidirectional WebSocket communication (Spring WebSocket)
- Stateful sessions with `ConcurrentHashMap` for thread-safety
- Server-side rendering + client-side Canvas display

#### 🐳 Cloud-Native & Container-Ready
- **Multi-stage Docker build** (Gradle builder + JRE runtime)
- **Dynamic port binding** via `$PORT` environment variable (Railway-compatible)
- **Production logging** with SLF4J (structured logs, configurable levels)
- **Graceful error handling** with client-side feedback

---

## 📂 Project Structure

```
src/main/
├── java/fr/esiee/baba/
│   ├── core/               # Abstractions (Renderer interface)
│   ├── model/              # Pure domain logic (Level, Rules, Transmutation, Element)
│   ├── controller/         # Game flow management (GameAction handling)
│   └── web/                # WebSocket adapter (GameWebSocketHandler, WebSocketConfig)
├── resources/
│   ├── static/             # Frontend (index.html, game.js, images/*.gif)
│   ├── text/               # Level files (level0.txt - level7.txt)
│   └── application.properties  # Spring Boot configuration (PORT, logging)
└── Dockerfile              # Multi-stage container build
```

### Module Responsibilities

| Module | Responsibility | Key Classes |
|--------|---------------|-------------|
| **core** | Rendering abstraction | `Renderer` |
| **model** | Game rules & state | `Level`, `Rules`, `Transmutation`, `Element`, `Cellule` |
| **controller** | Game lifecycle | `Game`, `GameAction` enum |
| **web** | WebSocket communication | `GameWebSocketHandler`, `WebSocketConfig` |
| **static** | HTML5 Canvas frontend | `game.js`, `index.html`, sprite images |

---

## 📦 Deployment

### 🌐 Railway Cloud Deployment (Production)

**Automatic Deployment:**
1. Push to GitHub branch: `claude/baba-is-you-backend-bFxtQ`
2. Railway detects changes and triggers build
3. Multi-stage Docker build executed
4. Application deployed with dynamic `$PORT`

**Configuration:**
- **Build:** `Dockerfile` (Gradle 8.5 + JDK 21 → Eclipse Temurin JRE 21)
- **Port:** Automatically injected via `$PORT` environment variable
- **Logs:** Available in Railway Dashboard (console output)

**Health Check:**
```bash
curl https://your-app.up.railway.app
# Should return index.html with game
```

---

### 🐳 Docker (Local Production Simulation)

**Build and Run:**
```bash
# Build multi-stage image
docker build -t baba-is-you .

# Run with port mapping
docker run -p 8080:8080 baba-is-you

# Run with custom port (simulate Railway)
docker run -e PORT=3000 -p 3000:3000 baba-is-you
```

**Access:** http://localhost:8080

---

### 💻 Local Development

**Prerequisites:**
- Java 21 (OpenJDK or Eclipse Temurin)
- Gradle 8.5+ (or use wrapper)

**Run:**
```bash
# Using Gradle wrapper (recommended)
./gradlew bootRun

# Or with installed Gradle
gradle bootRun
```

**Access:** http://localhost:8080

**Hot Reload:** Use Spring DevTools (add dependency) for automatic restarts on code changes.

---

## 🎮 How to Play

### Controls
- **Arrow Keys** (↑ ↓ ← →): Move entities with `YOU` property
- **R**: Restart current level (reloads to initial state)
- **Z**: Undo last move *(not implemented - use R to restart)*

### Game Rules Implemented

| Rule | Effect |
|------|--------|
| **BABA IS YOU** | Control Baba with arrow keys |
| **ROCK IS PUSH** | Rocks can be pushed |
| **WALL IS STOP** | Walls block movement |
| **FLAG IS WIN** | Touching flag completes level |
| **WATER IS SINK** | Entities disappear in water |
| **LAVA IS HOT** | Entities melt in lava |
| **SKULL IS DEFEAT** | Touching skull removes YOU entities |
| **ROCK IS WALL** | Transmutation - rocks become walls |

### Winning Strategy
1. Read the word tiles to understand active rules
2. Push word tiles to create new rules (e.g., move "ROCK" "IS" "WIN")
3. Use transmutation to transform objects
4. Reach the WIN condition

---

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test

# With coverage report
./gradlew test jacocoTestReport
```

### Test Coverage
- Rule parsing (horizontal/vertical)
- Entity movement logic
- Push mechanics
- Transmutation system

---

## 🚧 Known Limitations

### Not Implemented
- **Level progression persistence:** Current level resets on browser refresh
- **Multiplayer:** Each WebSocket session is independent
- **Mobile touch controls:** Arrow keys only

### Future Enhancements
- Level editor (create custom levels via UI)
- Leaderboard (completion time)
- Sound effects
- Animation smoothing
- Mobile-responsive controls

---

## 🛠️ Technology Stack

### Backend
- **Java 21** (Records, Pattern Matching, Text Blocks)
- **Spring Boot 3.2.1** (Embedded Tomcat)
- **Spring WebSocket** (STOMP over WebSocket)
- **Jackson** (JSON serialization)
- **SLF4J + Logback** (Structured logging)
- **Gradle 8.5** (Build automation)

### Frontend
- **HTML5 Canvas** (2D rendering)
- **Vanilla JavaScript** (ES6+)
- **WebSocket API** (Real-time communication)

### DevOps
- **Docker** (Multi-stage builds)
- **Railway** (Cloud platform)
- **Git** (Version control)

---

## 📊 Performance Considerations

### Optimizations Applied
- **Concurrent session management:** `ConcurrentHashMap` for thread-safe WebSocket sessions
- **Lazy level loading:** Levels parsed only on connection, not at startup
- **Minimal JSON payload:** Only grid state sent (not entire game object)
- **Client-side caching:** Images loaded once and reused

### Scalability
- **Stateful architecture:** Each player has isolated `GameSession`
- **Memory per session:** ~500KB (8 levels × 30×20 grid × object overhead)
- **Horizontal scaling:** Requires sticky sessions or shared state (Redis)

---

## 👨‍💻 Development

### Adding New Levels
1. Create `src/main/resources/text/level8.txt`
2. Use level file format (space-separated element codes)
3. Restart application (levels loaded at startup)

**Element Codes:**
```
B = ENTITY_BABA    b = BABA (word)
W = ENTITY_WALL    w = WALL (word)
F = ENTITY_FLAG    f = FLAG (word)
R = ENTITY_ROCK    r = ROCK (word)
A = ENTITY_WATER   a = WATER (word)
i = IS (operator)
y = YOU (property)
v = WIN (property)
t = STOP (property)
p = PUSH (property)
```

### Modifying Rules
Edit `src/main/java/fr/esiee/baba/model/Rules.java`:
- `initRules()`: Horizontal/vertical word scanning
- `nounToProperty`: Maps noun → active properties
- `transformationRules`: NOUN IS NOUN transformations

---

## 🏆 Project Achievements

### Software Engineering
✅ **Clean Architecture:** Decoupled domain logic from infrastructure
✅ **SOLID Principles:** Single Responsibility, Dependency Inversion
✅ **Design Patterns:** Strategy (Renderer), Factory (Element parsing), Observer (WebSocket)
✅ **Production Debugging:** Systematic issue diagnosis in cloud environment

### Cloud & DevOps
✅ **Containerization:** Docker multi-stage builds
✅ **Cloud Deployment:** Railway PaaS integration
✅ **Configuration Management:** Environment-based settings (PORT, logging)
✅ **Observability:** Structured logging, error tracking

### Real-Time Web
✅ **WebSocket Communication:** Bidirectional, low-latency
✅ **Session Management:** Thread-safe concurrent sessions
✅ **Error Handling:** Graceful degradation, user feedback

---

## 📄 License

Educational project - ESIEE Paris
Original author: **RAMANANJATOVO**
Production refactoring: **Claude Agent SDK (Anthropic)**

---

## 📞 Contact

**For recruiters:** This project demonstrates:
- Modern Java development (Java 21, Spring Boot 3)
- Full-stack capabilities (Backend + Frontend)
- Production debugging skills (systematic root cause analysis)
- Cloud deployment experience (Docker, Railway)
- Real-time communication (WebSockets)
- Clean architecture & design patterns

**GitHub Repository:** [[https://github.com/Whitemes/BabaIsYou](https://github.com/Whitemes/BabaIsYou)]
**Live Demo:** [[https://babaisyou-production.up.railway.app](https://babaisyou-production.up.railway.app)]
