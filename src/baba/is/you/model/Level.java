package baba.is.you.model;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Level {
    private List<List<Cellule>> grid;
    private Rules rules;
    private boolean completed;

    /**
     * Constructor for Level class
     *
     * @param grid the grid for the current level
     */
    public Level(List<List<Cellule>> grid) {
        this.grid = grid;
        this.rules = new Rules(this);
        this.completed = false;
    }

    /**
     * Get the grid for the current level
     *
     * @return the grid of cells
     */
    public List<List<Cellule>> getGrid() {
        return grid;
    }

    /**
     * Render the current grid to the console
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
     * Update the level based on the given direction
     *
     * @param direction the direction to move
     */
    public void update(Direction direction) {
        var dx = direction.getDx();
        var dy = direction.getDy();
        updateEntities(dx, dy);
        Transmutation transmutation = new Transmutation(this, rules);
        transmutation.checkMelt();
        transmutation.checkDefeat();
        transmutation.checkSink();
        rules.initRules(this);
        applyAllTransformations(transmutation);
    }

    private void applyAllTransformations(Transmutation transmutation) {
        var transformationRules = rules.getTransformationRules();
        for (var entry : transformationRules.entrySet()) {
            transmutation.applyTransformation(entry.getKey(), entry.getValue());
        }
    }

    private void updateEntities(int dx, int dy) {
        var moves = new HashSet<EntityMove>();
        for (var x = 0; x < grid.size(); x++) {
            for (var y = 0; y < grid.get(x).size(); y++) {
                if (!rules.getBabaElements(grid.get(x).get(y)).isEmpty()) {
                    moves.add(new EntityMove(x, y, dx, dy));
                }
            }
        }
        for (var move : moves) {
            handleEntityMove(move.x, move.y, move.dx, move.dy);
        }
    }

    private void handleEntityMove(int x, int y, int dx, int dy) {
        var newX = x + dx;
        var newY = y + dy;
        if (isWithinBounds(newX, newY)) {
            var targetCell = grid.get(newX).get(newY);
            if (rules.isWin(targetCell)) {
                System.out.println("You Win!");
                this.completed = true;
                return;
            }
            if (pushRecursive(x, y, newX, newY)) {
                var currentCell = grid.get(x).get(y);
                var babaElements = rules.getBabaElements(currentCell);
                var poppedElements = currentCell.popElements(babaElements);
                targetCell.getElements().addAll(poppedElements.getElements());
            }
        }
    }

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
     * Print the content of a specific cell
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
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
     * Print the content of all cells in the grid
     */
    public void printGridContent() {
        for (var x = 0; x < grid.size(); x++) {
            for (var y = 0; y < grid.get(x).size(); y++) {
                printCellContent(x, y);
            }
        }
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < grid.size() && y >= 0 && y < grid.get(0).size();
    }

    public boolean isCompleted() {
        return completed;
    }

    private static class EntityMove {
        int x, y, dx, dy;

        EntityMove(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }
    }
}
