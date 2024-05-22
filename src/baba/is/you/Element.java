package baba.is.you;

/**
 * Enum representing the different elements in the game
 */
public enum Element {
    BABA(Word.Noun.BABA),
    FLAG(Word.Noun.FLAG),
    WALL(Word.Noun.WALL),
    WATER(Word.Noun.WATER),
    SKULL(Word.Noun.SKULL),
    LAVA(Word.Noun.LAVA),
    ROCK(Word.Noun.ROCK),

    IS(Word.Operator.IS),

    YOU(Word.Property.YOU),
    WIN(Word.Property.WIN),
    STOP(Word.Property.STOP),
    PUSH(Word.Property.PUSH),
    MELT(Word.Property.MELT),
    HOT(Word.Property.HOT),
    DEFEAT(Word.Property.DEFEAT),
    SINK(Word.Property.SINK),

    ENTITY_BABA(Entity.BABA),
    ENTITY_FLAG(Entity.FLAG),
    ENTITY_WALL(Entity.WALL),
    ENTITY_WATER(Entity.WATER),
    ENTITY_SKULL(Entity.SKULL),
    ENTITY_LAVA(Entity.LAVA),
    ENTITY_ROCK(Entity.ROCK),
    EMPTY(Entity.EMPTY);

    private final Word word;
    private final Entity entity;

    /**
     * Constructor for Element enum with Noun
     * 
     * @param noun the noun associated with the element
     */
    Element(Word.Noun noun) {
        this.word = new Word(noun);
        this.entity = null;
    }

    /**
     * Constructor for Element enum with Operator
     * 
     * @param operator the operator associated with the element
     */
    Element(Word.Operator operator) {
        this.word = new Word(operator);
        this.entity = null;
    }

    /**
     * Constructor for Element enum with Property
     * 
     * @param property the property associated with the element
     */
    Element(Word.Property property) {
        this.word = new Word(property);
        this.entity = null;
    }

    /**
     * Constructor for Element enum with Entity
     * 
     * @param entity the entity associated with the element
     */
    Element(Entity entity) {
        this.word = null;
        this.entity = entity;
    }

    /**
     * Get the word associated with the element
     * 
     * @return the word
     */
    public Word getWord() {
        return word;
    }

    /**
     * Get the entity associated with the element
     * 
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Check if the element is a property
     * 
     * @return true if it is a property, false otherwise
     */
    public boolean isProperty() {
        return this.entity == null && this.word != null && this.word.getProperty() != null;
    }

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
            default: return "?";
        }
    }
}
