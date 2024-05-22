package baba.is.you;

import java.util.List;
import java.util.Set;

public class Level {
    private List<List<Element>> grid;
    private Rules rules;

    public Level(List<List<Element>> grid) {
        this.grid = grid;
        this.rules = new Rules(this);
    }

    public List<List<Element>> getGrid() {
        return grid;
    }

    public void render() {
        for (List<Element> row : grid) {
            for (Element element : row) {
                System.out.print(element.toString() + " ");
            }
            System.out.println();
        }
    }

    public void update(Direction direction) {
        int dx = direction.getDx();
        int dy = direction.getDy();

        Set<Element> winElements = rules.getWinElements();

        for (int x = 0; x < grid.size(); x++) {
            for (int y = 0; y < grid.get(x).size(); y++) {
                if (grid.get(x).get(y) == Element.ENTITY_BABA) {
                    int newX = x + dx;
                    int newY = y + dy;
                    if (isWithinBounds(newX, newY)) {
                        Element target = grid.get(newX).get(newY);
                        if (winElements.contains(target)) {
                            System.out.println("You Win!");
                            System.exit(0); // Exit the game
                        }
                        if (pushRecursive(x, y, newX, newY)) {
                            grid.get(x).set(y, Element.EMPTY);
                            grid.get(newX).set(newY, Element.ENTITY_BABA);
                        }
                    }
                    return;
                }
            }
        }
    }

    private boolean pushRecursive(int oldX, int oldY, int newX, int newY) {
        if (!isWithinBounds(newX, newY)) return false;
        Element target = grid.get(newX).get(newY);
        if (target == Element.EMPTY) return true;
        if (target == Element.ENTITY_WALL) return false;
        if (target == Element.ROCK || target == Element.FLAG || target.getWord() != null) {
            int dx = newX - oldX;
            int dy = newY - oldY;
            int nextX = newX + dx;
            int nextY = newY + dy;
            if (pushRecursive(newX, newY, nextX, nextY)) {
                grid.get(newX).set(newY, Element.EMPTY);
                grid.get(nextX).set(nextY, target);
                return true;
            }
        }
        return false;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < grid.size() && y >= 0 && y < grid.get(0).size();
    }

    public boolean isCompleted() {
        // This method can remain unchanged if it is not being used to detect win condition
        return false;
    }
}
