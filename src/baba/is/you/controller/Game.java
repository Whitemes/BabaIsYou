package baba.is.you.controller;

import baba.is.you.model.Cellule;
import baba.is.you.model.Direction;
import baba.is.you.model.Element;
import baba.is.you.model.Level;
import baba.is.you.model.Rules;
import baba.is.you.model.Transmutation;
import baba.is.you.view.View;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controls the overall game flow and interactions for the "BABA IS YOU" game.
 * This class manages the levels, player inputs, and game states as the player progresses through the game.
 */
public class Game {
    private final List<Level> levels;
    private int currentLevelIndex;
    private final Map<String, Element> elementMap;
    private ApplicationContext context;
    private View view;

    /**
     * Constructs a Game instance by loading all levels from a specified directory.
     *
     * @param levelDirectoryPath the path to the directory containing level files.
     */
    public Game(String levelDirectoryPath) {
        this.elementMap = createElementMap();
        this.levels = loadLevels(levelDirectoryPath);
        this.currentLevelIndex = 0;
    }

    /**
     * Initiates and handles the game loop, processing through levels until the game is completed.
     *
     * @param context the application context used for managing UI and interaction events.
     */
    public void start(ApplicationContext context) {
        this.context = context;
        playNextLevel();
    }

    /**
     * Plays the next level in the level sequence or terminates the game if all levels are completed.
     * This function sets up the game environment for the current level and begins the game loop for that level.
     */
    private void playNextLevel() {
        if (currentLevelIndex < levels.size()) {
            var level = levels.get(currentLevelIndex);
            var rules = new Rules(level);
            var transmutation = new Transmutation(level, rules);
            view = View.initGameGraphics(level.getGrid(), context.getScreenInfo().height(), context.getScreenInfo().width());
            playLevel(level, rules, transmutation);
        } else {
            System.out.println("\nCongratulations! You've completed the game.");
            System.exit(0);
        }
    }

    /**
     * Handles the gameplay loop for a single level, processing user inputs and updating the game state until the level is completed.
     *
     * @param level the level currently being played.
     * @param rules the rules object containing game logic for the current level.
     * @param transmutation the transmutation object handling dynamic changes in the game state.
     */
    private void playLevel(Level level, Rules rules, Transmutation transmutation) {
        while (!level.isCompleted()) {
            renderCurrentLevel();
            processInput(level, rules, transmutation);
        }
        currentLevelIndex++;
        playNextLevel();
    }

    /**
     * Processes user keyboard input, updates the game state based on the input, and applies game rules.
     *
     * @param level the level to update based on user input.
     * @param rules the rules to apply after input processing.
     * @param transmutation the transmutation operations to perform after rule application.
     */
    private void processInput(Level level, Rules rules, Transmutation transmutation) {
    	Objects.requireNonNull(level);
    	Objects.requireNonNull(rules);
    	Objects.requireNonNull(transmutation);
        var event = context.pollOrWaitEvent(100);
        if (event instanceof KeyboardEvent keyboardEvent && keyboardEvent.action() == KeyboardEvent.Action.KEY_PRESSED) {
            try {
                if (keyboardEvent.key() == KeyboardEvent.Key.ESCAPE) {
                    System.out.println("Game exited.");
                    System.exit(0);
                }
                Direction direction = switch (keyboardEvent.key()) {
                    case LEFT -> Direction.LEFT;
                    case RIGHT -> Direction.RIGHT;
                    case UP -> Direction.UP;
                    case DOWN -> Direction.DOWN;
                    default -> throw new IllegalArgumentException("Invalid key");
                };
                level.update(direction);
                rules.initRules(level);
                transmutation.setTransmutation(level, rules);
                checkLevelCompletion(level);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move. Use arrow keys for directions.");
            }
        }
    }

    /**
     * Checks if the current level has been completed and advances to the next level if so.
     *
     * @param level the level to check for completion.
     */
    private void checkLevelCompletion(Level level) {
    	Objects.requireNonNull(level);
        if (level.isCompleted()) {
            currentLevelIndex++;
            playNextLevel();
        }
    }

    /**
     * Renders the current game state to the screen.
     */
    private void renderCurrentLevel() {
        View.draw(context, levels.get(currentLevelIndex).getGrid(), view);
    }

    /**
     * Loads a single level from the given file path.
     *
     * @param path the path to the level file
     * @return the loaded level
     */
    private Level loadLevel(String path) {
    	Objects.requireNonNull(path);
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
        return new Level(grid);
    }

    /**
     * Loads all levels from a directory path and returns them as a list.
     * Each level is constructed from files in the specified directory.
     *
     * @param directoryPath the directory path containing the level files.
     * @return a list of loaded levels.
     */
    private List<Level> loadLevels(String directoryPath) {
    	Objects.requireNonNull(directoryPath);
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

    /**
     * Converts a line of text into a row of cells, parsing each token in the line as an element in the game.
     *
     * @param line the line of text to parse.
     * @return a list of cells parsed from the line.
     */
    private List<Cellule> parseLineToRow(String line) {
    	Objects.requireNonNull(line);
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

    /**
     * Maps a string token to a corresponding game element, based on predefined mappings.
     *
     * @param token the string token to map to an element.
     * @return the corresponding game element.
     */
    private Element stringToElement(String token) {
        return elementMap.getOrDefault(Objects.requireNonNull(token), Element.EMPTY);
    }

    /**
     * Creates and returns a map of string tokens to game elements.
     * This map is used to convert text representations of elements into their corresponding game objects.
     *
     * @return a map mapping string tokens to game elements.
     */
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
        map.put("B", Element.ENTITY_BABA);
        map.put("F", Element.ENTITY_FLAG);
        map.put("W", Element.ENTITY_WALL);
        map.put("A", Element.ENTITY_WATER);
        map.put("S", Element.ENTITY_SKULL);
        map.put("L", Element.ENTITY_LAVA);
        map.put("R", Element.ENTITY_ROCK);
        map.put("G", Element.ENTITY_GRASS);
        map.put("-", Element.EMPTY);
        map.put("o", Element.FLOWER);
        map.put("g", Element.GRASS);
        map.put("J", Element.ENTITY_TILE);
        map.put("j", Element.TILE);
        return map;
    }

    /**
     * Entry point for running the game application.
     *
     * @param args the command-line arguments, not used in this application.
     */
    public static void main(String[] args) {
        Application.run(Color.BLACK, t -> {
            var game = new Game("resources/text/levels");
            game.start(t);
        });
    }
}
