package baba.is.you.main;

import baba.is.you.*;
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

public class Game {
    private final List<Level> levels;
    private int currentLevelIndex;
    private final Map<String, Element> elementMap;
    private ApplicationContext context;
    private View view;

    /**
     * Constructor for Game class.
     *
     * @param levelDirectoryPath the path to the levels directory
     */
    public Game(String levelDirectoryPath) {
        this.elementMap = createElementMap();
        this.levels = loadLevels(levelDirectoryPath);
        this.currentLevelIndex = 0;
    }

    /**
     * Starts the game, iterating through each level until the game is completed.
     */
    public void start(ApplicationContext context) {
        this.context = context;
        playNextLevel();
    }

    /**
     * Plays the next level in the sequence.
     */
    private void playNextLevel() {
        if (currentLevelIndex < levels.size()) {
            Level level = levels.get(currentLevelIndex);
            Rules rules = new Rules(level);
            Transmutation transmutation = new Transmutation(level, rules);
            view = View.initGameGraphics(level.getGrid(), context.getScreenInfo().height(), context.getScreenInfo().width());
            playLevel(level, rules, transmutation);
        } else {
            System.out.println("\nCongratulations! You've completed the game.");
            System.exit(0);
        }
    }

    /**
     * Plays a single level.
     *
     * @param level the current level to be played
     * @param rules the rules applicable to the current level
     * @param transmutation the transmutation rules applicable to the current level
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
     * Processes the user input and updates the level accordingly.
     *
     * @param level the current level to be updated
     * @param rules the rules applicable to the current level
     * @param transmutation the transmutation rules applicable to the current level
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
     * Checks if the current level is completed.
     *
     * @param level the current level to be checked
     */
    private void checkLevelCompletion(Level level) {
        if (level.isCompleted()) {
            currentLevelIndex++;
            playNextLevel();
        }
    }

    /**
     * Renders the current level.
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
        List<List<Cellule>> grid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
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
     * Loads levels from the given directory path.
     *
     * @param directoryPath the path to the levels directory
     * @return a list of levels
     */
    private List<Level> loadLevels(String directoryPath) {
        List<Level> levels = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryPath), "*.txt")) {
            for (Path entry : stream) {
                levels.add(loadLevel(entry.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    /**
     * Parses a line of text into a row of cells.
     *
     * @param line the line of text to parse
     * @return a list of cells
     */
    private List<Cellule> parseLineToRow(String line) {
        List<Cellule> row = new ArrayList<>();
        String[] tokens = line.split(" ");
        for (String token : tokens) {
            Cellule cell = new Cellule();
            cell.addElement(Element.EMPTY); // Always add EMPTY element
            Element element = stringToElement(token);
            if (element != null && element != Element.EMPTY) {
                cell.addElement(element);
            }
            row.add(cell);
        }
        return row;
    }

    /**
     * Converts a string token to the corresponding element.
     *
     * @param token the string token
     * @return the corresponding element
     */
    private Element stringToElement(String token) {
        return elementMap.getOrDefault(token, Element.EMPTY);
    }

    /**
     * Creates a map for converting string tokens to elements.
     *
     * @return the map of string tokens to elements
     */
    private Map<String, Element> createElementMap() {
        Map<String, Element> map = new HashMap<>();
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
        map.put("x", Element.HOT);
        map.put("d", Element.DEFEAT);
        map.put("k", Element.SINK);
        map.put("B", Element.ENTITY_BABA);
        map.put("F", Element.ENTITY_FLAG);
        map.put("W", Element.ENTITY_WALL);
        map.put("A", Element.ENTITY_WATER);
        map.put("S", Element.ENTITY_SKULL);
        map.put("L", Element.ENTITY_LAVA);
        map.put("R", Element.ENTITY_ROCK);
        map.put("-", Element.EMPTY);
        map.put("o", Element.FLOWER);
        map.put("g", Element.GRASS);
        map.put("J", Element.ENTITY_TILE);
        map.put("j", Element.TILE);
        return map;
    }

    public static void main(String[] args) {
        Application.run(Color.BLACK, t -> {
            Game game = new Game("assets/text/levels");
            game.start(t);
        });
    }
}
