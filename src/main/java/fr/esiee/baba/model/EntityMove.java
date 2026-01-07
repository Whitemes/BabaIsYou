package fr.esiee.baba.model;

import java.util.Objects;

/**
 * Class to manage the movement of entities within the level grid.
 * This class holds coordinate and direction information for entity movements, along with the entity itself.
 */
public class EntityMove {
    int x, y, dx, dy;
    Element element;

    /**
     * Constructor for creating a movement directive for an entity.
     *
     * @param x the initial x-coordinate of the entity
     * @param y the initial y-coordinate of the entity
     * @param dx the x-direction increment (left or right)
     * @param dy the y-direction increment (up or down)
     * @param element the entity to be moved
     */
    public EntityMove(int x, int y, int dx, int dy, Element element) {
    	Objects.requireNonNull(element);
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.element = element;
    }
}
