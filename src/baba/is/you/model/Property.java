package baba.is.you.model;

/**
 * The Property enum represents various properties that elements in the game can have.
 * These properties define the behaviors and interactions of the elements.
 */
public enum Property {
    /**
     * The YOU property, indicating the player-controlled element.
     */
    YOU,
    /**
     * The WIN property, indicating the element that leads to victory.
     */
    WIN,
    /**
     * The STOP property, indicating an element that blocks movement.
     */
    STOP,
    /**
     * The PUSH property, indicating an element that can be pushed.
     */
    PUSH,
    /**
     * The MELT property, indicating an element that can be melted by HOT elements.
     */
    MELT,
    /**
     * The HOT property, indicating an element that melts MELT elements.
     */
    HOT,
    /**
     * The DEFEAT property, indicating an element that defeats YOU elements.
     */
    DEFEAT,
    /**
     * The SINK property, indicating an element that sinks when it shares a cell with another element.
     */
    SINK;
}
