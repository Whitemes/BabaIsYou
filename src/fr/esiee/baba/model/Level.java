package fr.esiee.baba.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Manages a single game level in "BABA IS YOU", including the game grid, rules, and game state.
 * This class encapsulates the logic required to interact with and modify the game environment.
 */
public class Level {
    private List<List<Cellule>> grid;
    private Rules rules;
    private boolean completed;
    private String levelFilePath;

    /**
     * Constructor for the Level class. Initializes the grid, sets up the rules, and marks the level as not completed.
     * 
     * @param grid the grid of cells that make up the level layout.
     * @param levelFilePath the file path of the level file.
     */
    public Level(List<List<Cellule>> grid, String levelFilePath) {
        this.grid = Objects.requireNonNull(grid);
        this.rules = new Rules(this);
        this.completed = false;
        this.levelFilePath = Objects.requireNonNull(levelFilePath);
    }

    /**
     * Retrieves the current state of the game grid.
     *
     * @return the grid, a list of lists containing cells.
     */
    public List<List<Cellule>> getGrid() {
        return grid;
    }

    /**
     * Retrieves the file path of the level file.
     *
     * @return the level file path.
     */
    public String getLevelFilePath() {
        return levelFilePath;
    }

    /**
     * Displays the current state of the grid to the console, representing each cell's contents.
     */
    public void render() {
        for (var row : grid) {
            for (var cell : row) {
                System.out.print(cell.toString() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Updates the game level based on player movement in the specified direction.
     * This includes moving entities, applying transformations, and reinitializing the rules.
     *
     * @param direction the direction of movement triggered by the player.
     * @param isJump whether the entity can jump or not.
     */
    public void update(Direction direction, boolean isJump) {
    	Objects.requireNonNull(direction);
        var dx = direction.getDx();
        var dy = direction.getDy();
        updateEntities(dx, dy, isJump);
        Transmutation transmutation = new Transmutation(this, rules);
        transmutation.checkMelt();
        transmutation.checkDefeat();
        transmutation.checkSink();
        rules.initRules(this);
        applyAllTransformations(transmutation);
    }

    /**
     * Applies all transformations based on the current rules after moving entities and updating the game state.
     * 
     * @param transmutation the Transmutation object handling the transformation processes.
     */
    private void applyAllTransformations(Transmutation transmutation) {
        var transformationRules = rules.getTransformationRules();
        for (var entry : transformationRules.entrySet()) {
            transmutation.applyTransformation(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Moves entities within the grid according to specified changes in x and y coordinates.
     *
     * @param dx the change in x coordinates (left or right).
     * @param dy the change in y coordinates (up or down).
     * @param isJump whether the entity can jump or not.
     */
    private void updateEntities(int dx, int dy, boolean isJump) {
        var moves = new HashSet<EntityMove>();
        for (var x = 0; x < grid.size(); x++) {
            for (var y = 0; y < grid.get(x).size(); y++) {
                var cell = grid.get(x).get(y);
                var youElements = rules.getYouElements(cell);
                if (!youElements.isEmpty()) {
                    for (var element : youElements) {
                        cell.removeElement(element);
                        if (isJump) {
                            moves.add(new EntityMove(x, y, 2 * dx, 2 * dy, element));
                        } else {
                            moves.add(new EntityMove(x, y, dx, dy, element));
                        }
                    }
                }
            }
        }
        for (var move : moves) {
            if (!handleEntityMove(move)) {
                grid.get(move.x).get(move.y).addElement(move.element); // Put back if move fails
            }
        }
    }

    /**
     * Handles the movement of an entity within the grid, checking for interactions such as win conditions,
     * pushing other entities, or stopping due to barriers.
     *
     * @param move the EntityMove containing the entity and its move details.
     * @return true if the entity was successfully moved, false otherwise.
     */
    private boolean handleEntityMove(EntityMove move) {
    	Objects.requireNonNull(move);
        var newX = move.x + move.dx;
        var newY = move.y + move.dy;
        if (isWithinBounds(newX, newY)) {
            var targetCell = grid.get(newX).get(newY);
            if (rules.isWin(targetCell)) {
                System.out.println("You Win!");
                this.completed = true;
                return true;
            }
            if (pushRecursive(move.x, move.y, newX, newY)) {
                targetCell.addElement(move.element);
                return true;
            }
        } else {
            if (newX < 0) newX = 0;
            if (newX >= grid.size()) newX = grid.size() - 1;
            if (newY < 0) newY = 0;
            if (newY >= grid.get(0).size()) newY = grid.get(0).size() - 1;
            grid.get(newX).get(newY).addElement(move.element);
            return true;
        }
        return false;
    }

    /**
     * Recursively attempts to push entities within the grid, checking if movement is possible and applying movement recursively.
     *
     * @param oldX the starting x-coordinate.
     * @param oldY the starting y-coordinate.
     * @param newX the intended new x-coordinate after pushing.
     * @param newY the intended new y-coordinate after pushing.
     * @return true if the entity can be pushed to the new location, false otherwise.
     */
    private boolean pushRecursive(int oldX, int oldY, int newX, int newY) {
        if (!isWithinBounds(newX, newY)) return false;
        var targetCell = grid.get(newX).get(newY);
        if (targetCell.isEmpty()) return true;
        if (!rules.getStopElements(targetCell).isEmpty()) return false;
        var pushableElements = rules.getPushableElements(targetCell);
        if (!pushableElements.isEmpty()) {
            var dx = newX - oldX;
            var dy = newY - oldY;
            var nextX = newX + dx;
            var nextY = newY + dy;
            if (isWithinBounds(nextX, nextY) && pushRecursive(newX, newY, nextX, nextY)) {
                var poppedElements = targetCell.popElements(pushableElements);
                grid.get(nextX).get(nextY).getElements().addAll(poppedElements.getElements());
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints the content of a specific cell at the given coordinates.
     * Useful for debugging or for displaying the cell state during gameplay.
     *
     * @param x the x-coordinate of the cell.
     * @param y the y-coordinate of the cell.
     */
    public void printCellContent(int x, int y) {
        if (isWithinBounds(x, y)) {
            var cell = grid.get(x).get(y);
            System.out.println("Content of cell (" + x + ", " + y + "): " + cell.getElements());
        } else {
            System.out.println("Cell (" + x + ", " + y + ") is out of bounds.");
        }
    }

    /**
     * Prints the contents of all cells in the grid.
     * This method loops through the entire grid and prints the contents of each cell.
     */
    public void printGridContent() {
        for (var x = 0; x < grid.size(); x++) {
            for (var y = 0; y < grid.get(x).size(); y++) {
                printCellContent(x, y);
            }
        }
    }

    /**
     * Checks if the specified coordinates are within the bounds of the grid.
     * This method prevents array index out-of-bounds errors when accessing grid cells.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if the coordinates are within the grid bounds, false otherwise.
     */
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < grid.size() && y >= 0 && y < grid.get(0).size();
    }

    /**
     * Checks if the level has been completed, i.e., if the win condition has been met.
     * 
     * @return true if the level is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Retrieves the elements that have the "YOU" property in the level.
     * 
     * @return a set of elements with the "YOU" property.
     */
    public Set<Element> getYouElements() {
        var youElements = new HashSet<Element>();
        for (var row : grid) {
            for (var cell : row) {
                youElements.addAll(rules.getYouElements(cell));
            }
        }
        return youElements;
    }
}
