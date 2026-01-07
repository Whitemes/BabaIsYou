package fr.esiee.baba.web;

import fr.esiee.baba.controller.Game;
import fr.esiee.baba.controller.Game.GameAction;
import fr.esiee.baba.core.Renderer;
import fr.esiee.baba.model.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Create a Renderer specific to this session
        Renderer sessionRenderer = level -> {
            try {
                if (session.isOpen()) {
                    // Serialize simplified grid state
                    // We might want a DTO instead of full circular object graph if any
                    // But Cellule -> Element is a tree, usually fine unless Element has back-ref
                    String json = objectMapper.writeValueAsString(level.getGrid());
                    session.sendMessage(new TextMessage(json));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        String levelPath = "src/main/resources/text"; // Default path
        Game game = new Game(levelPath, sessionRenderer);

        GameSession gameSession = new GameSession(game, session);
        sessions.put(session.getId(), gameSession);

        System.out.println("New session connected: " + session.getId());

        // Start game in a separate thread to not block WS handler?
        // Game.start() is a loop (if not Event Driven).
        // We refactored Game to be Event Driven (handleAction), but start() calls
        // loadNextLevel which calls render().
        game.start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GameSession gameSession = sessions.get(session.getId());
        if (gameSession == null)
            return;

        String payload = message.getPayload();
        try {
            GameAction action = GameAction.valueOf("MOVE_" + payload.toUpperCase());
            gameSession.game.handleAction(action);
        } catch (IllegalArgumentException e) {
            try {
                GameAction action = GameAction.valueOf(payload.toUpperCase());
                gameSession.game.handleAction(action);
            } catch (IllegalArgumentException ex) {
                System.out.println("Unknown command: " + payload);
            }
        }
    }

    private static class GameSession {
        Game game;
        WebSocketSession session;

        public GameSession(Game game, WebSocketSession session) {
            this.game = game;
            this.session = session;
        }
    }
}
