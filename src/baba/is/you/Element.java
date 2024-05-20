package baba.is.you;

public enum Element {
    BABA(Noun.BABA),
    FLAG(Noun.FLAG),
    WALL(Noun.WALL),
    WATER(Noun.WATER),
    SKULL(Noun.SKULL),
    LAVA(Noun.LAVA),
    ROCK(Noun.ROCK),

    IS(Operator.IS),

    YOU(Property.YOU),
    WIN(Property.WIN),
    STOP(Property.STOP),
    PUSH(Property.PUSH),
    MELT(Property.MELT),
    HOT(Property.HOT),
    DEFEAT(Property.DEFEAT),
    SINK(Property.SINK),

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

    Element(Noun noun) {
        this.word = new Word(noun);
        this.entity = null;
    }

    Element(Operator operator) {
        this.word = new Word(operator);
        this.entity = null;
    }

    Element(Property property) {
        this.word = new Word(property);
        this.entity = null;
    }

    Element(Entity entity) {
        this.word = null;
        this.entity = entity;
    }

    public Word getWord() {
        return word;
    }

    public Entity getEntity() {
        return entity;
    }

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
