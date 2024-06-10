package fr.esiee.baba.model;

/**
 * Class to manage the movement of entities within the level grid.
 * This class holds coordinate and direction information for entity movements.
 */
public class EntityMove {
    int x, y, dx, dy;
    
    /**
     * Constructor for creating a movement directive for an entity.
     *
     * @param x the initial x-coordinate of the entity
     * @param y the initial y-coordinate of the entity
     * @param dx the x-direction increment (left or right)
     * @param dy the y-direction increment (up or down)
     */
    EntityMove(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
}
