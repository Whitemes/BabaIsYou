package fr.esiee.baba.controller;

import fr.esiee.baba.core.Renderer;
import fr.esiee.baba.model.*;

import java.util.*;

/**
 * Controls the overall game flow for "BABA IS YOU".
 * Refactored to be Event-Driven for WebSockets and Container-Ready.
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
     * Constructs a Game instance with pre-loaded levels.
     *
     * @param levels   list of loaded levels.
     * @param renderer output for rendering.
     */
    public Game(List<Level> levels, Renderer renderer) {
        this.elementMap = createElementMap();
        this.levels = levels;
        this.currentLevelIndex = 0;
        this.renderer = renderer;
    }

    /**
     * Parses a list of strings into a Level object.
     * 
     * @param lines the lines of the level file.
     * @param name  identifier for the level.
     * @return the constructed Level.
     */
    public static Level parseLevel(List<String> lines, String name) {
        var grid = new ArrayList<List<Cellule>>();
        Game parser = new Game(new ArrayList<>(), null); // dummy for map access? Or make map static?
        // Actually, the map logic is inside Game instance. Let's make helper static or
        // expose it.
        // Better: static helper for parsing.

        for (String line : lines) {
            if (!line.isEmpty()) {
                grid.add(parseLineToRow(line));
            }
        }
        return new Level(grid, name);
    }

    private static List<Cellule> parseLineToRow(String line) {
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

    public static Element stringToElement(String token) {
        // We can recreate the map or look it up.
        // For simplicity, let's just use the static map method here for now to avoid
        // instance dependency.
        return STATIC_ELEMENT_MAP.getOrDefault(token, Element.EMPTY);
    }

    // Static map for parsing
    private static final Map<String, Element> STATIC_ELEMENT_MAP = createElementMapStatic();

    private static Map<String, Element> createElementMapStatic() {
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

    // Instance map (kept for compatibility or future dynamic use)
    private Map<String, Element> createElementMap() {
        return STATIC_ELEMENT_MAP;
    }

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
            rules.initRules(level);
            Transmutation transmutation = new Transmutation(level, rules);
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
        Rules rules = new Rules(level);
        Transmutation transmutation = new Transmutation(level, rules);

        Direction direction = null;
        switch (action) {
            case QUIT -> {
                isFinished = true;
                return;
            }
            case RESTART -> {
                // Logic to reset the current level would go here
                // For now, simple return
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
                renderer.render(level);
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
