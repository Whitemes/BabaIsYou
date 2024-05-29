package baba.is.you;

/**
 * The Element enum represents various elements in the game.
 * Each element can be associated with a Word or an Entity.
 */
public enum Element {
    /**
     * Represents the player character.
     */
    BABA(Word.Noun.BABA),
    /**
     * Represents the goal object.
     */
    FLAG(Word.Noun.FLAG),
    /**
     * Represents an impassable wall.
     */
    WALL(Word.Noun.WALL),
    /**
     * Represents a water obstacle.
     */
    WATER(Word.Noun.WATER),
    /**
     * Represents a deadly obstacle.
     */
    SKULL(Word.Noun.SKULL),
    /**
     * Represents a lava obstacle.
     */
    LAVA(Word.Noun.LAVA),
    /**
     * Represents a movable rock.
     */
    ROCK(Word.Noun.ROCK),
    /**
     * Represents a flower object.
     */
    FLOWER(Word.Noun.FLOWER),
    /**
     * Represents a grass object.
     */
    GRASS(Word.Noun.GRASS),
    /**
     * Represents a tile object.
     */
    TILE(Word.Noun.TILE),
    /**
     * Represents the "IS" operator in rules.
     */
    IS(Word.Operator.IS),

    /**
     * Represents the "YOU" property in rules.
     */
    YOU(Word.Property.YOU),
    /**
     * Represents the "WIN" property in rules.
     */
    WIN(Word.Property.WIN),
    /**
     * Represents the "STOP" property in rules.
     */
    STOP(Word.Property.STOP),
    /**
     * Represents the "PUSH" property in rules.
     */
    PUSH(Word.Property.PUSH),
    /**
     * Represents the "MELT" property in rules.
     */
    MELT(Word.Property.MELT),
    /**
     * Represents the "HOT" property in rules.
     */
    HOT(Word.Property.HOT),
    /**
     * Represents the "DEFEAT" property in rules.
     */
    DEFEAT(Word.Property.DEFEAT),
    /**
     * Represents the "SINK" property in rules.
     */
    SINK(Word.Property.SINK),

    /**
     * Represents the player character entity.
     */
    ENTITY_BABA(Entity.BABA),
    /**
     * Represents the goal entity.
     */
    ENTITY_FLAG(Entity.FLAG),
    /**
     * Represents an impassable wall entity.
     */
    ENTITY_WALL(Entity.WALL),
    /**
     * Represents a water obstacle entity.
     */
    ENTITY_WATER(Entity.WATER),
    /**
     * Represents a deadly obstacle entity.
     */
    ENTITY_SKULL(Entity.SKULL),
    /**
     * Represents a lava obstacle entity.
     */
    ENTITY_LAVA(Entity.LAVA),
    /**
     * Represents a movable rock entity.
     */
    ENTITY_ROCK(Entity.ROCK),
    /**
     * Represents a flower entity.
     */
    ENTITY_FLOWER(Entity.FLOWER),
    /**
     * Represents a grass entity.
     */
    ENTITY_GRASS(Entity.GRASS),
    /**
     * Represents a tile entity.
     */
    ENTITY_TILE(Entity.TILE),

    /**
     * Represents an empty space entity.
     */
    EMPTY(Entity.EMPTY);

    private final Word word;
    private final Entity entity;

    /**
     * Constructor for elements associated with a noun.
     *
     * @param noun the noun associated with the element
     */
    Element(Word.Noun noun) {
        this.word = new Word(noun);
        this.entity = null;
    }

    /**
     * Constructor for elements associated with an operator.
     *
     * @param operator the operator associated with the element
     */
    Element(Word.Operator operator) {
        this.word = new Word(operator);
        this.entity = null;
    }

    /**
     * Constructor for elements associated with a property.
     *
     * @param property the property associated with the element
     */
    Element(Word.Property property) {
        this.word = new Word(property);
        this.entity = null;
    }

    /**
     * Constructor for elements associated with an entity.
     *
     * @param entity the entity associated with the element
     */
    Element(Entity entity) {
        this.word = null;
        this.entity = entity;
    }

    /**
     * Gets the word associated with the element.
     *
     * @return the associated word, or null if none
     */
    public Word getWord() {
        return word;
    }

    /**
     * Gets the entity associated with the element.
     *
     * @return the associated entity, or null if none
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Checks if the element is a property.
     *
     * @return true if the element is a property, false otherwise
     */
    public boolean isProperty() {
        return this.entity == null && this.word != null && this.word.getProperty() != null;
    }

    /**
     * Returns a string representation of the element.
     *
     * @return the string representation of the element
     */
    @Override
    public String toString() {
        if (word != null) {
            return word.toString().substring(0, 1).toLowerCase();
        }
        switch (this) {
            case ENTITY_BABA: return "B";
            case ENTITY_FLAG: return "F";
            case ENTITY_WALL: return "W";
            case ENTITY_WATER: return "A";
            case ENTITY_SKULL: return "S";
            case ENTITY_LAVA: return "L";
            case ENTITY_ROCK: return "R";
            case EMPTY: return "-";
            case ENTITY_FLOWER: return "o";
            case ENTITY_GRASS: return "g";
            case ENTITY_TILE: return "J";
            default: return "?";
        }
    }
}
