package baba.is.you;

public class Word {
    private final Noun noun;
    private final Operator operator;
    private final Property property;

    public Word(Noun noun) {
        this.noun = noun;
        this.operator = null;
        this.property = null;
    }

    public Word(Operator operator) {
        this.noun = null;
        this.operator = operator;
        this.property = null;
    }

    public Word(Property property) {
        this.noun = null;
        this.operator = null;
        this.property = property;
    }

    public Noun getNoun() {
        return noun;
    }

    public Operator getOperator() {
        return operator;
    }

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
