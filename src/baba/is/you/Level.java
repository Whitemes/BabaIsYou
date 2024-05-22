package baba.is.you;

import java.util.List;
import java.util.Set;

public class Level {
    private List<List<Element>> grid;
    private Rules rules;

    /**
     * Constructor for Level class
     * 
     * @param grid the grid for the current level
     */
    public Level(List<List<Element>> grid) {
        this.grid = grid;
        this.rules = new Rules(this);
    }

    /**
     * Get the grid for the current level
     * 
     * @return the grid of elements
     */
    public List<List<Element>> getGrid() {
        return grid;
    }

    /**
     * Render the current grid to the console
     */
    public void render() {
        for (List<Element> row : grid) {
            for (Element element : row) {
                System.out.print(element.toString() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Update the level based on the given direction
     * 
     * @param direction the direction to move
     */
    public void update(Direction direction) {
        int dx = direction.getDx();
        int dy = direction.getDy();
        Set<Element> winElements = rules.getWinElements();
        updateEntities(dx, dy, winElements);
    }

    /**
     * Update the entities in the grid based on the direction
     *
     * @param dx         the change in x-coordinate
     * @param dy         the change in y-coordinate
     * @param winElements the set of elements that result in a win
     */
    private void updateEntities(int dx, int dy, Set<Element> winElements) {
        for (int x = 0; x < grid.size(); x++) {
            for (int y = 0; y < grid.get(x).size(); y++) {
                if (grid.get(x).get(y) == Element.ENTITY_BABA) {
                    handleEntityMove(x, y, dx, dy, winElements);
                    return;
                }
            }
        }
    }

    /**
     * Handle the movement of an entity in the grid
     *
     * @param x          the original x-coordinate
     * @param y          the original y-coordinate
     * @param dx         the change in x-coordinate
     * @param dy         the change in y-coordinate
     * @param winElements the set of elements that result in a win
     */
    private void handleEntityMove(int x, int y, int dx, int dy, Set<Element> winElements) {
        int newX = x + dx;
        int newY = y + dy;
        if (isWithinBounds(newX, newY)) {
            Element target = grid.get(newX).get(newY);
            if (winElements.contains(target)) {
                System.out.println("You Win!");
                System.exit(0);
            }
            if (pushRecursive(x, y, newX, newY)) {
                grid.get(x).set(y, Element.EMPTY);
                grid.get(newX).set(newY, Element.ENTITY_BABA);
            }
        }
    }

    /**
     * Recursively push elements in the grid
     * 
     * @param oldX the original x-coordinate
     * @param oldY the original y-coordinate
     * @param newX the new x-coordinate
     * @param newY the new y-coordinate
     * @return true if the push was successful, false otherwise
     */
    private boolean pushRecursive(int oldX, int oldY, int newX, int newY) {
        if (!isWithinBounds(newX, newY)) return false;
        Element target = grid.get(newX).get(newY);
        if (target == Element.EMPTY) return true;
        if (target == Element.ENTITY_WALL) return false;
        if (isPushable(target)) {
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

    /**
     * Check if the given element can be pushed
     *
     * @param element the element to check
     * @return true if the element can be pushed, false otherwise
     */
    private boolean isPushable(Element element) {
        return element.getWord() != null;
    }

    /**
     * Check if the given coordinates are within the bounds of the grid
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if within bounds, false otherwise
     */
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < grid.size() && y >= 0 && y < grid.get(0).size();
    }

    /**
     * Check if the level is completed
     * 
     * @return true if the level is completed, false otherwise
     */
    public boolean isCompleted() {
        // This method can remain unchanged if it is not being used to detect win condition
        return false;
    }
}
