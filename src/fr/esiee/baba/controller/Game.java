package fr.esiee.baba.controller;

import fr.esiee.baba.model.*;
import fr.esiee.baba.view.View;
import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

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
     * Constructs a Game instance by loading a single level.
     *
     * @param levelFilePath the path to the level file.
     */
    public Game(String levelFilePath, boolean isSingleLevel) {
        this.elementMap = createElementMap();
        this.levels = new ArrayList<>();
        this.levels.add(loadLevel(levelFilePath));
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
                boolean isJump = rules.hasProperty(level.getYouElements(), Property.JUMP);
                level.update(direction, isJump);
                rules.initRules(level);
                transmutation.setTransmutation(level, rules);
                checkLevelCompletion(level);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move. Use arrow keys for directions.");
            }
        }
    }
    
//    /**
//     * Processes input events within the game, handling player actions based on different types of input events.
//     * Currently, it supports keyboard events for player controls.
//     *
//     * @param level the current level of the game where the action takes place.
//     * @param rules the set of rules governing the game mechanics.
//     * @param transmutation the mechanism to apply rule-based transformations.
//     */
//    private void processInput(Level level, Rules rules, Transmutation transmutation) {
//        var event = context.pollOrWaitEvent(100); 
//        switch (event) {
//            case KeyboardEvent keyboardEvent -> {
//            	 if (keyboardEvent.action() == KeyboardEvent.Action.KEY_PRESSED) {
//                     try {
//                         Direction direction = switch (keyboardEvent.key()) {
//                             case LEFT -> Direction.LEFT;
//                             case RIGHT -> Direction.RIGHT;
//                             case UP -> Direction.UP;
//                             case DOWN -> Direction.DOWN;
//                             default -> throw new IllegalArgumentException("Invalid key");
//                         };
//
//                         boolean isJump = rules.hasProperty(level.getYouElements(), Property.JUMP);
//                         level.update(direction, isJump);
//                         rules.initRules(level);
//                         transmutation.setTransmutation(level, rules);
//                         checkLevelCompletion(level);
//                     } catch (IllegalArgumentException e) {
//                         System.out.println("Invalid move. Use arrow keys for directions.");
//                     }
//                     
//                     if (keyboardEvent.key() == KeyboardEvent.Key.ESCAPE) {
//                         System.out.println("Game exited.");
//                         System.exit(0);
//                     }
//                 }
//            }
//            default -> {}
//        }
//    }

    /**
     * Checks if the current level has been completed and advances to the next level if so.
     *
     * @param level the level to check for completion.
     */
    private void checkLevelCompletion(Level level) {
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

    /**
     * Loads all levels from a directory path and returns them as a list.
     * Each level is constructed from files in the specified directory.
     *
     * @param directoryPath the directory path containing the level files.
     * @return a list of loaded levels.
     */
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

    /**
     * Converts a line of text into a row of cells, parsing each token in the line as an element in the game.
     *
     * @param line the line of text to parse.
     * @return a list of cells parsed from the line.
     */
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

    /**
     * Maps a string token to a corresponding game element, based on predefined mappings.
     *
     * @param token the string token to map to an element.
     * @return the corresponding game element.
     */
    private Element stringToElement(String token) {
        return elementMap.getOrDefault(token, Element.EMPTY);
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
        map.put("x", Element.ENTITY_SMILEY);
        map.put("-", Element.EMPTY);
        map.put("o", Element.FLOWER);
        map.put("g", Element.GRASS);
        map.put("J", Element.ENTITY_TILE);
        map.put("j", Element.TILE);
        return map;
    }

    /**
     * The main entry point of the application, which parses command-line arguments to configure and launch the game.
     * This method supports launching the game with specific levels or directories specified, and executing additional
     * rules based on the command-line inputs.
     *
     * The command-line arguments can specify a single level file, a directory of levels, or a default level that is 
     * used if no specific level or directory is provided. Additionally, it supports executing custom rules
     * that are applied at the start of the game.
     *
     * @param args the command-line arguments used to control game configuration. Supported options include:
     *             --level [path] : Specifies the path to a single level file to load.
     *             --levels [directory] : Specifies a directory from which to load all levels.
     *             --execute [element1 IS element2] : Applies a custom rule, where element1 and element2
     *                                               are specified elements to create a dynamic rule at runtime.
     * 
     * Example:
     *   java -jar baba.jar --level "path/to/level.txt" : Loads and starts the game using the specified level file.
     *   java -jar baba.jar --levels "path/to/levels/" : Loads all levels from the specified directory.
     *   java -jar baba.jar --execute "Rock IS Win" : Applies the rule that turns all rocks into a winning condition.
     */
    public static void main(String[] args) {
        var options = CommandLineParser.parse(args);
        Game game = null;

        if (options.containsKey("--level")) {
            var levelPath = options.get("--level").get(0).get(0);
            game = new Game(levelPath, true);
        } else if (options.containsKey("--levels")) {
            var levelDirectory = options.get("--levels").get(0).get(0);
            game = new Game(levelDirectory);
        } else {
            var defaultLevelPath = Paths.get("resources", "text", "defaultLevel", "default-level.txt").toAbsolutePath().toString();
            game = new Game(defaultLevelPath, true);
        }

        Game finalGame = game;
        Application.run(Color.BLACK, t -> {
            finalGame.start(t);

            if (options.containsKey("--execute")) {
                for (var executeArgs : options.get("--execute")) {
                    var rules = new Rules(null);
                    rules.addRule(
                            finalGame.stringToElement(executeArgs.get(0)),
                            Element.IS,
                            finalGame.stringToElement(executeArgs.get(2))
                    );
                }
            }
        });
    }

}
