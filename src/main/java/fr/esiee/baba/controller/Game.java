package fr.esiee.baba.controller;

import fr.esiee.baba.core.Renderer;
import fr.esiee.baba.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Controls the overall game flow for "BABA IS YOU".
 * Refactored to be Event-Driven for WebSockets.
 */
public class Game {
    private final List<Level> levels;
    private int currentLevelIndex;
    private final Map<String, Element> elementMap;
    private final Renderer renderer;
    private boolean isFinished = false;

    public enum GameAction {
        MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT,
        WAIT, UNDO, RESTART, QUIT
    }

    /**
     * Constructs a Game instance.
     *
     * @param levelDirectoryPath path to levels.
     * @param renderer           output for rendering.
     */
    public Game(String levelDirectoryPath, Renderer renderer) {
        this.elementMap = createElementMap();
        this.levels = loadLevels(levelDirectoryPath);
        this.currentLevelIndex = 0;
        this.renderer = renderer;
    }

    /**
     * Single level constructor.
     */
    public Game(String levelFilePath, boolean isSingleLevel, Renderer renderer) {
        this.elementMap = createElementMap();
        this.levels = new ArrayList<>();
        this.levels.add(loadLevel(levelFilePath));
        this.currentLevelIndex = 0;
        this.renderer = renderer;
    }

    /**
     * Starts the main game loop.
     */
    public void start() {
        if (levels.isEmpty()) {
            System.out.println("No levels found.");
            return;
        }
        loadCurrentLevel();
    }

    private void loadCurrentLevel() {
        if (currentLevelIndex < levels.size()) {
            Level level = levels.get(currentLevelIndex);
            Rules rules = new Rules(level);
            // Initial init
            rules.initRules(level);
            Transmutation transmutation = new Transmutation(level, rules);

            // Store these if needed, currently recreated on update?
            // Actually, level keeps state. Rules need to be refreshed.
            // Transmutation is transient per update usually.

            renderer.render(level);
        } else {
            isFinished = true;
            System.out.println("All levels completed!");
        }
    }

    public void handleAction(GameAction action) {
        if (isFinished || currentLevelIndex >= levels.size())
            return;

        Level level = levels.get(currentLevelIndex);
        Rules rules = new Rules(level); // Re-evaluate rules? Or keep persistent?
        // Original code created new Rules(level) every playNextLevel.
        // But inside the loop it did rules.initRules(level).

        Transmutation transmutation = new Transmutation(level, rules);

        Direction direction = null;
        switch (action) {
            case QUIT -> {
                isFinished = true;
                return;
            }
            case RESTART -> {
                // Reload level logic would go here
                return;
            }
            case MOVE_LEFT -> direction = Direction.LEFT;
            case MOVE_RIGHT -> direction = Direction.RIGHT;
            case MOVE_UP -> direction = Direction.UP;
            case MOVE_DOWN -> direction = Direction.DOWN;
            default -> {
            }
        }

        if (direction != null) {
            boolean isJump = rules.hasProperty(level.getYouElements(), Property.JUMP);
            level.update(direction, isJump);
            rules.initRules(level);
            transmutation.setTransmutation(level, rules);

            if (level.isCompleted()) {
                currentLevelIndex++;
                if (currentLevelIndex < levels.size()) {
                    loadCurrentLevel();
                } else {
                    isFinished = true;
                    System.out.println("Game Completed!");
                }
            } else {
                renderer.render(level); // Render updated state
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    // --- Data Loading Methods (Unchanged mostly) ---

    private Level loadLevel(String path) {
        var grid = new ArrayList<List<Cellule>>();
        try (var reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    grid.add(parseLineToRow(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Level(grid, path);
    }

    private List<Level> loadLevels(String directoryPath) {
        var levels = new ArrayList<Level>();
        try (var stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.txt")) {
            for (var entry : stream) {
                levels.add(loadLevel(entry.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    private List<Cellule> parseLineToRow(String line) {
        var row = new ArrayList<Cellule>();
        var tokens = line.split(" ");
        for (var token : tokens) {
            var cell = new Cellule();
            cell.addElement(Element.EMPTY);
            var element = stringToElement(token);
            if (element != null && element != Element.EMPTY) {
                cell.addElement(element);
            }
            row.add(cell);
        }
        return row;
    }

    public Element stringToElement(String token) {
        return elementMap.getOrDefault(token, Element.EMPTY);
    }

    private Map<String, Element> createElementMap() {
        var map = new HashMap<String, Element>();
        map.put("b", Element.BABA);
        map.put("f", Element.FLAG);
        map.put("w", Element.WALL);
        map.put("a", Element.WATER);
        map.put("s", Element.SKULL);
        map.put("l", Element.LAVA);
        map.put("r", Element.ROCK);
        map.put("i", Element.IS);
        map.put("y", Element.YOU);
        map.put("v", Element.WIN);
        map.put("t", Element.STOP);
        map.put("p", Element.PUSH);
        map.put("m", Element.MELT);
        map.put("h", Element.HOT);
        map.put("d", Element.DEFEAT);
        map.put("k", Element.SINK);
        map.put("n", Element.SMILEY);
        map.put("u", Element.JUMP);
        map.put("B", Element.ENTITY_BABA);
        map.put("F", Element.ENTITY_FLAG);
        map.put("W", Element.ENTITY_WALL);
        map.put("A", Element.ENTITY_WATER);
        map.put("S", Element.ENTITY_SKULL);
        map.put("L", Element.ENTITY_LAVA);
        map.put("R", Element.ENTITY_ROCK);
        map.put("G", Element.ENTITY_GRASS);
        map.put("X", Element.ENTITY_SMILEY);
        map.put("-", Element.EMPTY);
        map.put("o", Element.FLOWER);
        map.put("g", Element.GRASS);
        map.put("T", Element.ENTITY_TILE);
        map.put("j", Element.TILE);
        return map;
    }

    // Main method removed or needs to be adapted to launch via Spring or CLI
    // wrapper
}
