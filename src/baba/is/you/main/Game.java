package baba.is.you.main;

import baba.is.you.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Game {
    private final List<Level> levels;
    private int currentLevelIndex;
    private final Scanner scanner;
    private final Map<String, Element> elementMap;

    /**
     * Constructor for Game class.
     *
     * @param levelFilePath the path to the levels file
     */
    public Game(String levelFilePath) {
        this.scanner = new Scanner(System.in);
        this.elementMap = createElementMap();
        this.levels = loadLevels(levelFilePath);
        this.currentLevelIndex = 0;
    }

    /**
     * Starts the game, iterating through each level until the game is completed.
     */
    public void start() {
        while (currentLevelIndex < levels.size()) {
            Level level = levels.get(currentLevelIndex);
            Rules rules = new Rules(level);
            playLevel(level, rules);
        }
        scanner.close();
    }

    /**
     * Plays a single level.
     *
     * @param level the current level to be played
     * @param rules the rules applicable to the current level
     */
    private void playLevel(Level level, Rules rules) {
        while (!level.isCompleted()) {
            renderCurrentLevel();
            char input = getUserInput();
            processInput(level, rules, input);
        }
    }

    /**
     * Processes the user input and updates the level accordingly.
     *
     * @param level the current level to be updated
     * @param rules the rules applicable to the current level
     * @param input the user input for the direction
     */
    private void processInput(Level level, Rules rules, char input) {
        try {
            Direction direction = Direction.fromChar(input);
            level.update(direction);
            rules.initRules(level);
            rules.printRules();
            checkLevelCompletion();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid move. Use 'z' (up), 's' (down), 'q' (left), 'd' (right).");
        }
    }

    /**
     * Checks if the current level is completed and moves to the next level if it is.
     */
    private void checkLevelCompletion() {
        if (levels.get(currentLevelIndex).isCompleted()) {
            currentLevelIndex++;
            if (currentLevelIndex < levels.size()) {
                renderCurrentLevel();
            } else {
                System.out.println("\nCongratulations! You've completed the game.");
            }
        }
    }

    /**
     * Renders the current level.
     */
    private void renderCurrentLevel() {
        levels.get(currentLevelIndex).render();
    }

    /**
     * Gets user input for the next move.
     *
     * @return the input character representing the direction
     */
    private char getUserInput() {
        System.out.print("Enter your move (z for up, s for down, q for left, d for right): ");
        String input = scanner.nextLine();
        return input.length() > 0 ? input.charAt(0) : ' ';
    }

    /**
     * Loads levels from the given file path.
     *
     * @param path the path to the levels file
     * @return a list of levels
     */
    private List<Level> loadLevels(String path) {
        List<Level> levels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            loadLevelsFromFile(reader, levels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    /**
     * Loads levels from the BufferedReader and adds them to the levels list.
     *
     * @param reader the BufferedReader to read the levels from
     * @param levels the list to store the loaded levels
     * @throws IOException if an I/O error occurs
     */
    private void loadLevelsFromFile(BufferedReader reader, List<Level> levels) throws IOException {
        String line;
        List<List<Element>> grid = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                addGridToLevels(levels, grid);
                grid = new ArrayList<>();
            } else {
                grid.add(parseLineToRow(line));
            }
        }
        addGridToLevels(levels, grid);
    }

    /**
     * Adds the grid to the levels list if it is not empty.
     *
     * @param levels the list of levels
     * @param grid the grid to be added
     */
    private void addGridToLevels(List<Level> levels, List<List<Element>> grid) {
        if (!grid.isEmpty()) {
            levels.add(new Level(grid));
        }
    }

    /**
     * Parses a line of text into a row of elements.
     *
     * @param line the line of text to parse
     * @return a list of elements
     */
    private List<Element> parseLineToRow(String line) {
        List<Element> row = new ArrayList<>();
        String[] tokens = line.split(" ");
        for (String token : tokens) {
            row.add(elementMap.getOrDefault(token, Element.EMPTY));
        }
        return row;
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
        return map;
    }

    public static void main(String[] args) {
        Game game = new Game("assets/text/level1.txt");
        game.start();
    }
}
