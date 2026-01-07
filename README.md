# Baba Is You - Engineering Edition

A professional reimplementation of the "Baba Is You" puzzle game engine, focusing on **Software Architecture**, **Clean Code**, and **Cloud Deployment**.

![Status](https://img.shields.io/badge/Status-Complete-success)
![Stack](https://img.shields.io/badge/Tech-Java%2021%20%7C%20Spring%20Boot%20%7C%20WebSockets%20%7C%20Docker-blue)

## 🚀 Project Overview

This project transforms a legacy academic codebase into a robust, deployable web application.
It demonstrates **logic/view separation**, **event-driven design**, and **containerization**.

### Key Engineering Features
- **Hexagonal-like Architecture**: Core game logic (`model`) is completely decoupled from the rendering layer (`web`).
- **Event-Driven Game Loop**: The game state evolves based on `GameAction` events processed via WebSockets.
- **headless-ready**: The core engine runs without any graphical dependencies (AWT/Swing removed), making it perfect for cloud servers.
- **Tests**: Comprehensive JUnit 5 tests validation of rule parsing and movement logic.

## 🛠️ Architecture

```mermaid
graph TD
    Client[Web Client (HTML5/Canvas)] <-->|WebSocket/JSON| Handler[GameWebSocketHandler]
    Handler --> Controller[Game Controller]
    Controller --> Logic[Core Logic (Rules/Level)]
    Logic --> Model[Data Model (Cellule/Element)]
```

### Modules
- **`fr.esiee.baba.core`**: Abstractions for Input and Rendering.
- **`fr.esiee.baba.model`**: Pure domain logic (Rules, Transmutation, Grid).
- **`fr.esiee.baba.controller`**: Manages the game flow.
- **`fr.esiee.baba.web`**: WebSocket adapter for the browser frontend.

## 📦 Deployment

The project is containerized using Docker and built with Gradle.

### Running with Docker (Recommended)
No local Java/Gradle required.

```bash
# Build and Run
docker build -t baba-is-you .
docker run -p 8080:8080 baba-is-you
```

Access the game at: `http://localhost:8080`

### Local Development
Prerequisites: Java 21.

```bash
# MacOS/Linux
./gradlew bootRun

# Windows
gradle bootRun
```

## 🧪 Testing

The logic is verified using JUnit 5.

```bash
gradle test
```

## 📝 Rules Implemented
- **Movement**: `YOU` objects move on user input.
- **Push**: `PUSH` objects move if pushed by `YOU`.
- **Win**: `YOU` on `WIN` triggers victory.
- **Defeat**: `YOU` on `DEFEAT` triggers removal.
- **Transform**: `NOUN IS NOUN` transforms entities (e.g., `ROCK IS WALL`).

## 👤 Author
**RAMANANJATOVO** - ESIEE Paris
refactored by **Antigravity Agent**
