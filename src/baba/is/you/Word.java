package baba.is.you;

/**
 * Class representing a word in the game
 */
public class Word {
	
    /**
     * Enum representing nouns
     */
    public enum Noun {
        BABA, FLAG, WALL, WATER, SKULL, LAVA, ROCK;
    }
	
    /**
     * Enum representing operators
     */
    public enum Operator {
        IS;
    }
	
    /**
     * Enum representing properties
     */
    public enum Property {
        YOU, WIN, STOP, PUSH, MELT, HOT, DEFEAT, SINK;
    }
	
    private final Noun noun;
    private final Operator operator;
    private final Property property;

    /**
     * Constructor for Word with Noun
     * 
     * @param noun the noun
     */
    public Word(Noun noun) {
        this.noun = noun;
        this.operator = null;
        this.property = null;
    }

    /**
     * Constructor for Word with Operator
     * 
     * @param operator the operator
     */
    public Word(Operator operator) {
        this.noun = null;
        this.operator = operator;
        this.property = null;
    }

    /**
     * Constructor for Word with Property
     * 
     * @param property the property
     */
    public Word(Property property) {
        this.noun = null;
        this.operator = null;
        this.property = property;
    }

    /**
     * Get the noun of the word
     * 
     * @return the noun
     */
    public Noun getNoun() {
        return noun;
    }

    /**
     * Get the operator of the word
     * 
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Get the property of the word
     * 
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    @Override
    public String toString() {
        if (noun != null) {
            return noun.name();
        } else if (operator != null) {
            return operator.name();
        } else if (property != null) {
            return property.name();
        }
        return "UNKNOWN";
    }
}
