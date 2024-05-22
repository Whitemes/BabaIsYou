package baba.is.you.main;

import baba.is.you.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Game {
    private List<Level> levels;
    private int currentLevelIndex;

    public Game(String levelsPath) {
        this.levels = loadLevels(levelsPath);
        this.currentLevelIndex = 0;
    }

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
                    rules.initRules(level); // Reinitialiser les règles après chaque mouvement
                    rules.printRules(); // Afficher les règles actuelles
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
    }

    private void renderCurrentLevel() {
        Level level = levels.get(currentLevelIndex);
        level.render();
    }

    private char getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your move (z for up, s for down, q for left, d for right): ");
        String input = scanner.nextLine();
        return input.length() > 0 ? input.charAt(0) : ' ';
    }

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
                        row.add(stringToElement(token));
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

    private Element stringToElement(String token) {
        switch (token) {
            case "b": return Element.BABA;
            case "f": return Element.FLAG;
            case "w": return Element.WALL;
            case "a": return Element.WATER;
            case "s": return Element.SKULL;
            case "l": return Element.LAVA;
            case "r": return Element.ROCK;
            case "i": return Element.IS;
            case "y": return Element.YOU;
            case "v": return Element.WIN;
            case "t": return Element.STOP;
            case "p": return Element.PUSH;
            case "m": return Element.MELT;
            case "x": return Element.HOT;
            case "d": return Element.DEFEAT;
            case "k": return Element.SINK;
            case "B": return Element.ENTITY_BABA;
            case "F": return Element.ENTITY_FLAG;
            case "W": return Element.ENTITY_WALL;
            case "A": return Element.ENTITY_WATER;
            case "S": return Element.ENTITY_SKULL;
            case "L": return Element.ENTITY_LAVA;
            case "R": return Element.ENTITY_ROCK;
            case "-": return Element.EMPTY;
            default: return Element.EMPTY;
        }
    }

    public static void main(String[] args) {
        Game game = new Game("assets/text/level1.txt");
        game.start();
    }
}
