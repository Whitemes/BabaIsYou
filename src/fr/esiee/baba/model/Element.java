package fr.esiee.baba.model;

/**
 * The Element enum represents various elements in the game "BABA IS YOU".
 * Each element can be either a game entity, a rule word (noun, operator, or property), or a combination thereof.
 * This classification enables the elements to interact differently within the game's logic and rules.
 */
public enum Element {
    /**
     * Represents the player character.
     */
    BABA(Noun.BABA),
    /**
     * Represents the goal object.
     */
    FLAG(Noun.FLAG),
    /**
     * Represents an impassable wall.
     */
    WALL(Noun.WALL),
    /**
     * Represents a water obstacle.
     */
    WATER(Noun.WATER),
    /**
     * Represents a deadly obstacle.
     */
    SKULL(Noun.SKULL),
    /**
     * Represents a lava obstacle.
     */
    LAVA(Noun.LAVA),
    /**
     * Represents a movable rock.
     */
    ROCK(Noun.ROCK),
    /**
     * Represents a flower object.
     */
    FLOWER(Noun.FLOWER),
    /**
     * Represents a grass object.
     */
    GRASS(Noun.GRASS),
    /**
     * Represents a tile object.
     */
    TILE(Noun.TILE),
    /**
     * Represents the "IS" operator in rules.
     */
    IS(Operator.IS),

    /**
     * Represents the "YOU" property in rules.
     */
    YOU(Property.YOU),
    /**
     * Represents the "WIN" property in rules.
     */
    WIN(Property.WIN),
    /**
     * Represents the "STOP" property in rules.
     */
    STOP(Property.STOP),
    /**
     * Represents the "PUSH" property in rules.
     */
    PUSH(Property.PUSH),
    /**
     * Represents the "MELT" property in rules.
     */
    MELT(Property.MELT),
    /**
     * Represents the "HOT" property in rules.
     */
    HOT(Property.HOT),
    /**
     * Represents the "DEFEAT" property in rules.
     */
    DEFEAT(Property.DEFEAT),
    /**
     * Represents the "SINK" property in rules.
     */
    SINK(Property.SINK),

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
     * Constructor for elements defined by an operator.
     *
     * @param operator the associated operator
     */
    Element(Noun noun) {
    	
        this.word = new Word(noun);
        this.entity = null;
    }

    /**
     * Constructor for elements associated with an operator.
     *
     * @param operator the operator associated with the element
     */
    Element(Operator operator) {
        this.word = new Word(operator);
        this.entity = null;
    }

    /**
     * Constructor for elements defined by a property.
     *
     * @param property the associated property
     */
    Element(Property property) {
        this.word = new Word(property);
        this.entity = null;
    }

    /**
     * Constructor for elements defined by an entity.
     *
     * @param entity the associated entity
     */
    Element(Entity entity) {
        this.word = null;
        this.entity = entity;
    }

    /**
     * Retrieves the word associated with the element, if applicable.
     *
     * @return the word associated with this element, or null if the element is defined by an entity alone
     */
    public Word getWord() {
        return word;
    }

    /**
     * Retrieves the entity associated with the element, if applicable.
     *
     * @return the entity associated with this element, or null if the element is defined by a word alone
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Determines whether the element is defined as a property.
     *
     * @return true if this element represents a property, false otherwise
     */
    public boolean isProperty() {
        return this.entity == null && this.word != null && this.word.getProperty() != null;
    }

//    /**
//     * Returns a string representation of the element.
//     *
//     * @return the string representation of the element
//     */
//    @Override
//    public String toString() {
//        if (word != null) {
//            return word.toString().substring(0, 1).toLowerCase();
//        }
//        switch (this) {
//            case ENTITY_BABA: return "B";
//            case ENTITY_FLAG: return "F";
//            case ENTITY_WALL: return "W";
//            case ENTITY_WATER: return "A";
//            case ENTITY_SKULL: return "S";
//            case ENTITY_LAVA: return "L";
//            case ENTITY_ROCK: return "R";
//            case EMPTY: return "-";
//            case ENTITY_FLOWER: return "o";
//            case ENTITY_GRASS: return "g";
//            case ENTITY_TILE: return "J";
//            default: return "?";
//        }
//    }

}
