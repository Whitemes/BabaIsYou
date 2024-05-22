package baba.is.you;

/**
 * Enum representing the possible directions of movement
 */
public enum Direction {
    HAUT(-1, 0),
    BAS(1, 0),
    GAUCHE(0, -1),
    DROITE(0, 1);

    private final int dx;
    private final int dy;

    /**
     * Constructor for Direction enum
     * 
     * @param dx the change in x-coordinate
     * @param dy the change in y-coordinate
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the change in x-coordinate for the direction
     * 
     * @return the change in x-coordinate
     */
    public int getDx() {
        return dx;
    }

    /**
     * Get the change in y-coordinate for the direction
     * 
     * @return the change in y-coordinate
     */
    public int getDy() {
        return dy;
    }

    /**
     * Convert an input character to the corresponding direction
     * 
     * @param input the input character
     * @return the corresponding Direction
     * @throws IllegalArgumentException if the input is invalid
     */
    public static Direction fromChar(char input) {
        switch (input) {
            case 'z': return HAUT;
            case 's': return BAS;
            case 'q': return GAUCHE;
            case 'd': return DROITE;
            default: throw new IllegalArgumentException("Invalid direction: " + input);
        }
    }
}
