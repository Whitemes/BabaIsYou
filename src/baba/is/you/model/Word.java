package baba.is.you.model;

/**
 * The Word class represents a word in the game, which can be a noun, an operator, or a property.
 */
public class Word {
	
    private final Noun noun;
    private final Operator operator;
    private final Property property;

    /**
     * Constructor for creating a Word object representing a noun.
     *
     * @param noun the noun to be represented by this Word object
     */
    public Word(Noun noun) {
        this.noun = noun;
        this.operator = null;
        this.property = null;
    }

    /**
     * Constructor for creating a Word object representing an operator.
     *
     * @param operator the operator to be represented by this Word object
     */
    public Word(Operator operator) {
        this.noun = null;
        this.operator = operator;
        this.property = null;
    }

    /**
     * Constructor for creating a Word object representing a property.
     *
     * @param property the property to be represented by this Word object
     */
    public Word(Property property) {
        this.noun = null;
        this.operator = null;
        this.property = property;
    }

    /**
     * Gets the noun represented by this Word object.
     *
     * @return the noun, or null if this Word does not represent a noun
     */
    public Noun getNoun() {
        return noun;
    }

    /**
     * Gets the operator represented by this Word object.
     *
     * @return the operator, or null if this Word does not represent an operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Gets the property represented by this Word object.
     *
     * @return the property, or null if this Word does not represent a property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Returns a string representation of this Word object.
     *
     * @return the string representation of the noun, operator, or property
     */
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
