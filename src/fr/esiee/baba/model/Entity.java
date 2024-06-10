package fr.esiee.baba.model;

/**
 * The Entity enum represents the different types of entities in the game.
 * Each entity corresponds to a physical object or state in the game world.
 */
public enum Entity {
    /**
     * Represents the player character.
     */
    BABA,
    /**
     * Represents the goal object.
     */
    FLAG,
    /**
     * Represents an impassable wall.
     */
    WALL,
    /**
     * Represents a water obstacle.
     */
    WATER,
    /**
     * Represents a deadly obstacle.
     */
    SKULL,
    /**
     * Represents a lava obstacle.
     */
    LAVA,
    /**
     * Represents a movable rock.
     */
    ROCK,
    /**
     * Represents an empty space.
     */
    EMPTY,
    /**
     * Represents a flower object.
     */
    FLOWER,
    /**
     * Represents a grass object.
     */
    GRASS,
    /**
     * Represents a tile object.
     */
    TILE;
}
