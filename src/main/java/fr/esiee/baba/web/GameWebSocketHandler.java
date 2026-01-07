package fr.esiee.baba.web;

import fr.esiee.baba.controller.Game;
import fr.esiee.baba.controller.Game.GameAction;
import fr.esiee.baba.core.Renderer;
import fr.esiee.baba.model.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ResourcePatternResolver resourceResolver;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("WebSocket connection established - Session ID: {}", session.getId());

        // Create a Renderer specific to this session
        Renderer sessionRenderer = level -> {
            try {
                if (session.isOpen()) {
                    String json = objectMapper.writeValueAsString(level.getGrid());
                    session.sendMessage(new TextMessage(json));
                    logger.debug("Sent game state to client - Session: {}", session.getId());
                }
            } catch (IOException e) {
                logger.error("Failed to send game state to client - Session: {}", session.getId(), e);
            }
        };

        // Load levels from classpath
        List<Level> levels = new ArrayList<>();
        try {
            logger.debug("Loading levels from classpath...");

            // Try text/ directory first (primary location)
            Resource[] resources = resourceResolver.getResources("classpath:text/*.txt");

            // Fallback to static/text/ if nothing found
            if (resources == null || resources.length == 0) {
                logger.debug("No levels found in classpath:text/, trying classpath:static/text/");
                resources = resourceResolver.getResources("classpath:static/text/*.txt");
            }

            if (resources != null && resources.length > 0) {
                logger.info("Found {} level files", resources.length);

                // Sort resources to ensure level order
                Arrays.sort(resources, Comparator.comparing(Resource::getFilename));

                for (Resource res : resources) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()))) {
                        List<String> lines = reader.lines().toList();
                        Level level = Game.parseLevel(lines, res.getFilename());
                        levels.add(level);
                        logger.debug("Loaded level: {}", res.getFilename());
                    } catch (Exception e) {
                        logger.error("Failed to parse level file: {}", res.getFilename(), e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Critical error loading levels from classpath", e);
        }

        if (levels.isEmpty()) {
            logger.error("CRITICAL: No levels loaded! Client will see black screen.");
            // Send error state to client
            try {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "No game levels found on server");
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(errorResponse)));
            } catch (IOException e) {
                logger.error("Failed to send error message to client", e);
            }
            session.close();
            return;
        }

        logger.info("Successfully loaded {} levels for session {}", levels.size(), session.getId());

        Game game = new Game(levels, sessionRenderer);
        GameSession gameSession = new GameSession(game, session);
        sessions.put(session.getId(), gameSession);

        logger.info("Starting game for session: {}", session.getId());
        game.start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        GameSession gameSession = sessions.get(session.getId());
        if (gameSession == null) {
            logger.warn("Received message for unknown session: {}", session.getId());
            return;
        }

        String payload = message.getPayload();
        logger.debug("Received action from client - Session: {}, Action: {}", session.getId(), payload);

        try {
            GameAction action = GameAction.valueOf("MOVE_" + payload.toUpperCase());
            gameSession.game.handleAction(action);
        } catch (IllegalArgumentException e) {
            try {
                GameAction action = GameAction.valueOf(payload.toUpperCase());
                gameSession.game.handleAction(action);
            } catch (IllegalArgumentException ex) {
                logger.warn("Unknown command received: {} from session: {}", payload, session.getId());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        logger.info("WebSocket connection closed - Session: {}, Status: {}", session.getId(), status);
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
