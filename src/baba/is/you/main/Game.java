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
     * Constructor for Game class
     *
     * @param levelsPath the path to the levels file
     */
    public Game(String levelsPath) {
        this.scanner = new Scanner(System.in);
        this.elementMap = createElementMap(); 
        this.levels = loadLevels(levelsPath);
        this.currentLevelIndex = 0;
    }

    /**
     * Start the game
     */
    public void start() {
        while (currentLevelIndex < levels.size()) {
            Level level = levels.get(currentLevelIndex);
            Rules rules = new Rules(level);
            while (!level.isCompleted()) {
                renderCurrentLevel();
                char input = getUserInput();
                try {
                    Direction direction = Direction.fromChar(input);
                    level.update(direction);
                    rules.initRules(level); // Reinitialize rules after each move
                    rules.printRules(); // Print current rules
                    if (level.isCompleted()) {
                        currentLevelIndex++;
                        if (currentLevelIndex < levels.size()) {
                            renderCurrentLevel();
                        } else {
                            System.out.println("\nCongratulations! You've completed the game.");
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid move. Use 'z' (up), 's' (down), 'q' (left), 'd' (right).");
                }
            }
        }
        scanner.close();
    }

    /**
     * Render the current level
     */
    private void renderCurrentLevel() {
        Level level = levels.get(currentLevelIndex);
        level.render();
    }

    /**
     * Get user input for the next move
     *
     * @return the input character
     */
    private char getUserInput() {
        System.out.print("Enter your move (z for up, s for down, q for left, d for right): ");
        String input = scanner.nextLine();
        return input.length() > 0 ? input.charAt(0) : ' ';
    }

    /**
     * Load levels from the given file path
     *
     * @param path the path to the levels file
     * @return a list of levels
     */
    private List<Level> loadLevels(String path) {
        List<Level> levels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            List<List<Element>> grid = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!grid.isEmpty()) {
                        levels.add(new Level(grid));
                        grid = new ArrayList<>();
                    }
                } else {
                    List<Element> row = new ArrayList<>();
                    String[] tokens = line.split(" ");
                    for (String token : tokens) {
                        row.add(elementMap.getOrDefault(token, Element.EMPTY));
                    }
                    grid.add(row);
                }
            }
            if (!grid.isEmpty()) {
                levels.add(new Level(grid));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    /**
     * Create a map for element string to Element conversion
     *
     * @return the map of string tokens to Elements
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
