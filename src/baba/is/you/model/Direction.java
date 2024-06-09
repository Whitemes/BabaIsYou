package baba.is.you.model;

/**
 * The Direction enum represents the four cardinal directions (UP, DOWN, LEFT, RIGHT).
 * Each direction is associated with a change in the x and y coordinates (dx and dy).
 */
public enum Direction {
    /**
     * The UP direction, represented by a decrease in the y-coordinate.
     */
    UP(-1, 0),
    /**
     * The DOWN direction, represented by an increase in the y-coordinate.
     */
    DOWN(1, 0),
    /**
     * The LEFT direction, represented by a decrease in the x-coordinate.
     */
    LEFT(0, -1),
    /**
     * The RIGHT direction, represented by an increase in the x-coordinate.
     */
    RIGHT(0, 1);

    private final int dx;
    private final int dy;

    /**
     * Constructor for Direction enum.
     *
     * @param dx the change in the x-coordinate for the direction
     * @param dy the change in the y-coordinate for the direction
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Gets the change in the x-coordinate for the direction.
     *
     * @return the change in the x-coordinate
     */
    public int getDx() {
        return dx;
    }

    /**
     * Gets the change in the y-coordinate for the direction.
     *
     * @return the change in the y-coordinate
     */
    public int getDy() {
        return dy;
    }

    /**
     * Obtains the direction corresponding to a character input.
     *
     * @param c the character input
     * @return the corresponding direction
     * @throws IllegalArgumentException if the character does not correspond to a direction
     */
    public static Direction fromChar(char c) {
        return switch (c) {
            case 'z' -> UP;
            case 's' -> DOWN;
            case 'q' -> LEFT;
            case 'd' -> RIGHT;
            default -> throw new IllegalArgumentException("Invalid direction: " + c);
        };
    }
}
