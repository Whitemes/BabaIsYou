package baba.is.you;

public enum Direction {
    HAUT(-1, 0),
    BAS(1, 0),
    GAUCHE(0, -1),
    DROITE(0, 1);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

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
