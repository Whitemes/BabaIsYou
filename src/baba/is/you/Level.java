package baba.is.you;

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
        for (List<Cellule> row : grid) {
            for (Cellule cell : row) {
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
        int dx = direction.getDx();
        int dy = direction.getDy();
        updateEntities(dx, dy);
        Transmutation transmutation = new Transmutation(this, rules);
        transmutation.checkMelt();
        transmutation.checkDefeat();
        transmutation.checkSink();
        rules.initRules(this);
        applyAllTransformations(transmutation);
    }

    private void applyAllTransformations(Transmutation transmutation) {
        Map<Word.Noun, Word.Noun> transformationRules = rules.getTransformationRules();
        for (Map.Entry<Word.Noun, Word.Noun> entry : transformationRules.entrySet()) {
            transmutation.applyTransformation(entry.getKey(), entry.getValue());
        }
    }

    private void updateEntities(int dx, int dy) {
        Set<EntityMove> moves = new HashSet<>();
        for (int x = 0; x < grid.size(); x++) {
            for (int y = 0; y < grid.get(x).size(); y++) {
                if (!rules.getBabaElements(grid.get(x).get(y)).isEmpty()) {
                    moves.add(new EntityMove(x, y, dx, dy));
                }
            }
        }
        for (EntityMove move : moves) {
            handleEntityMove(move.x, move.y, move.dx, move.dy);
        }
    }

    private void handleEntityMove(int x, int y, int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        if (isWithinBounds(newX, newY)) {
            Cellule targetCell = grid.get(newX).get(newY);
            if (rules.isWin(targetCell)) {
                System.out.println("You Win!");
                this.completed = true;
                return;
            }
            if (pushRecursive(x, y, newX, newY)) {
                Cellule currentCell = grid.get(x).get(y);
                Set<Element> babaElements = rules.getBabaElements(currentCell);
                Cellule poppedElements = currentCell.popElements(babaElements);
                targetCell.getElements().addAll(poppedElements.getElements());
            }
        }
    }

    private boolean pushRecursive(int oldX, int oldY, int newX, int newY) {
        if (!isWithinBounds(newX, newY)) {
            return false;
        }
        Cellule targetCell = grid.get(newX).get(newY);
        if (targetCell.isEmpty()) {
            return true;
        }
        if (!rules.getStopElements(targetCell).isEmpty()) {
            return false;
        }
        Set<Element> pushableElements = rules.getPushableElements(targetCell);
        if (!pushableElements.isEmpty()) {
            int dx = newX - oldX;
            int dy = newY - oldY;
            int nextX = newX + dx;
            int nextY = newY + dy;
            if (isWithinBounds(nextX, nextY) && pushRecursive(newX, newY, nextX, nextY)) {
                Cellule poppedElements = targetCell.popElements(pushableElements); // Use popElements to move elements
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
            Cellule cell = grid.get(x).get(y);
            System.out.println("Content of cell (" + x + ", " + y + "): " + cell.getElements());
        } else {
            System.out.println("Cell (" + x + ", " + y + ") is out of bounds.");
        }
    }

    /**
     * Print the content of all cells in the grid
     */
    public void printGridContent() {
        for (int x = 0; x < grid.size(); x++) {
            for (int y = 0; y < grid.get(x).size(); y++) {
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
