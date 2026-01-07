package fr.esiee.baba.model;

import java.util.*;

/**
 * Manages and evaluates the rules that dictate the interactions and behaviors of elements
 * within the level in "BABA IS YOU". This class maps nouns to properties, properties to nouns,
 * and handles transformations and other dynamic changes in the game state.
 */
public class Rules {
    private Map<Element, Set<Property>> nounToProperty;
    private Map<Property, Set<Element>> propertyToNoun;
    private Map<Noun, Noun> transformationRules;

    /**
     * Constructs a new Rules object associated with a given level.
     * 
     * @param level the game level associated with these rules, used to initialize rule mappings based on the level's grid.
     */
    public Rules(Level level) {
        this.nounToProperty = new HashMap<>();
        this.propertyToNoun = new HashMap<>();
        this.transformationRules = new HashMap<>();
        initRules(level);
    }

    /**
     * Initializes or re-initializes the rules based on the current game level's grid.
     * This method scans the grid to establish new rule mappings.
     * 
     * @param level the level whose grid is scanned to establish rules.
     */
    public void initRules(Level level) {
        nounToProperty.clear();
        propertyToNoun.clear();
        transformationRules.clear();
        var grid = level.getGrid();
        for (var i = 0; i < grid.size(); i++) {
            for (var j = 0; j < grid.get(i).size(); j++) {
                if (isOperatorIs(grid, i, j)) {
                    checkVerticalRules(grid, i, j);
                    checkHorizontalRules(grid, i, j);
                }
            }
        }
    }

    /**
     * Evaluates potential vertical rules based on a central 'IS' operator positioned between two elements.
     *
     * @param grid the current grid of cells being evaluated.
     * @param i the row index of the 'IS' operator.
     * @param j the column index of the 'IS' operator.
     */
    private void checkVerticalRules(List<List<Cellule>> grid, int i, int j) {
        if (i > 0 && i < grid.size() - 1) {
            var above = grid.get(i - 1).get(j);
            var below = grid.get(i + 1).get(j);
            if (isValidRule(above, below)) {
                if (above.hasNoun() && below.hasNoun()) {
                    addTransformationRule(above.getNoun(), below.getNoun());
                } else {
                    addRule(getEntityByNoun(above.getNoun()), below.getProperty());
                }
            }
        }
    }

    /**
     * Evaluates potential horizontal rules based on a central 'IS' operator positioned between two elements.
     *
     * @param grid the current grid of cells being evaluated.
     * @param i the row index of the cell containing the 'IS' operator.
     * @param j the column index of the 'IS' operator.
     */
    private void checkHorizontalRules(List<List<Cellule>> grid, int i, int j) {
        if (j > 0 && j < grid.get(i).size() - 1) {
            var left = grid.get(i).get(j - 1);
            var right = grid.get(i).get(j + 1);
            if (isValidRule(left, right)) {
                if (left.hasNoun() && right.hasNoun()) {
                    addTransformationRule(left.getNoun(), right.getNoun());
                } else {
                    addRule(getEntityByNoun(left.getNoun()), right.getProperty());
                }
            }
        }
    }

    /**
     * Checks if the combination of two adjacent cells can form a valid rule based on game mechanics.
     * 
     * @param first the first cell involved in the potential rule.
     * @param second the second cell involved in the potential rule.
     * @return true if a valid rule can be formed, false otherwise.
     */
    private boolean isValidRule(Cellule first, Cellule second) {
        return first.hasNoun() && (second.hasProperty() || second.hasNoun());
    }

    /**
     * Adds a mapping from a noun to a property, enabling behaviors associated with the property to be applied to the noun.
     * 
     * @param noun the noun element that will gain the specified property.
     * @param property the property that is to be assigned to the noun.
     */
    private void addRule(Element noun, Property property) {
        nounToProperty.computeIfAbsent(noun, k -> new HashSet<>()).add(property);
        propertyToNoun.computeIfAbsent(property, k -> new HashSet<>()).add(noun);
    }

    /**
     * Adds a transformation rule between two nouns, allowing the first noun to transform into the second upon rule activation.
     * 
     * @param firstNoun the original noun.
     * @param secondNoun the noun into which the first noun can transform.
     */
    private void addTransformationRule(Noun firstNoun, Noun secondNoun) {
        transformationRules.put(firstNoun, secondNoun);
    }

    /**
     * Adds a rule based on three elements where the middle element is always the operator 'IS'.
     *
     * @param first the first element (noun)
     * @param second the second element (operator 'IS')
     * @param third the third element (noun or property)
     */
    public void addRule(Element first, Element second, Element third) {
        if (second != Element.IS) {
            throw new IllegalArgumentException("The second element must be the operator IS.");
        }

        if (first.getWord() != null && third.getWord() != null) {
            if (third.getWord().getProperty() != null) {
                addRule(first, third.getWord().getProperty());
            } else if (third.getWord().getNoun() != null) {
                addTransformationRule(first.getWord().getNoun(), third.getWord().getNoun());
            } else {
                throw new IllegalArgumentException("The third element must be a noun or property.");
            }
        } else {
            throw new IllegalArgumentException("The first and third elements must be words.");
        }
    }

    /**
     * Determines if the 'IS' operator is present in a specified cell.
     * 
     * @param grid the grid to check.
     * @param i the row index of the cell.
     * @param j the column index of the cell.
     * @return true if the 'IS' operator is present, false otherwise.
     */
    private boolean isOperatorIs(List<List<Cellule>> grid, int i, int j) {
        return grid.get(i).get(j).containsOperatorIs();
    }

    /**
     * Prints all mappings from nouns to properties and properties to nouns, as well as any transformation rules.
     * This method is useful for debugging and verifying the current state of the game's rules.
     */
    public void printMaps() {
        System.out.println("Noun to Property Map:");
        for (var entry : nounToProperty.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\nProperty to Noun Map:");
        for (var entry : propertyToNoun.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\nTransformation Rules:");
        for (var entry : transformationRules.entrySet()) {
            System.out.println(entry.getKey() + " IS " + entry.getValue());
        }
    }

    /**
     * Retrieves the corresponding game element for a given noun.
     * 
     * @param noun the noun whose corresponding game element is needed.
     * @return the corresponding game element, typically an entity or an empty value if no mapping exists.
     * @throws IllegalArgumentException if the noun is unrecognized, ensuring all nouns have valid mappings.
     */
    public static Element getEntityByNoun(Noun noun) {
        switch (noun) {
            case BABA:
                return Element.ENTITY_BABA;
            case FLAG:
                return Element.ENTITY_FLAG;
            case WALL:
                return Element.ENTITY_WALL;
            case WATER:
                return Element.ENTITY_WATER;
            case SKULL:
                return Element.ENTITY_SKULL;
            case LAVA:
                return Element.ENTITY_LAVA;
            case ROCK:
                return Element.ENTITY_ROCK;
            case SMILEY:
            	return Element.ENTITY_SMILEY;
            default:
                throw new IllegalArgumentException("Unknown noun: " + noun);
        }
    }

    /**
     * Retrieves a set of elements with a specified property within a given cell.
     *
     * @param cell the cell from which to retrieve elements based on a specified property.
     * @param property the property to check within the cell.
     * @return a set of elements that have the specified property.
     */
    public Set<Element> getEntitiesByProperty(Cellule cell, Property property) {
        var entities = new HashSet<Element>();
        var propertyEntities = propertyToNoun.get(property);
        if (propertyEntities != null) {
            for (var element : cell.getElements()) {
                if (propertyEntities.contains(element)) {
                    entities.add(element);
                }
            }
        }
        return entities;
    }

    /**
     * Retrieves the set of elements in a cell that can lead to a win condition.
     * 
     * @param cell the cell to check for winning elements.
     * @return a set of elements with the WIN property.
     */
    public Set<Element> getWinElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.WIN);
    }

    /**
     * Retrieves the set of elements in a cell that can be pushed according to the current game rules.
     * 
     * @param cell the cell to check for pushable elements.
     * @return a set of pushable elements.
     */
    public Set<Element> getPushableElements(Cellule cell) {
        var pushableElements = getEntitiesByProperty(cell, Property.PUSH);
        for (var element : cell.getElements()) {
            if (element.getWord() != null) {
                pushableElements.add(element); // All words are considered pushable
            }
        }
        return pushableElements;
    }

    /**
     * Retrieves the set of elements in a cell that prevent movement, acting as barriers.
     * 
     * @param cell the cell to check for stopping elements.
     * @return a set of elements with the STOP property.
     */
    public Set<Element> getStopElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.STOP);
    }

    /**
     * Retrieves the set of elements in a cell designated as "YOU", indicating player-controlled elements.
     * 
     * @param cell the cell to check for elements controlled by the player.
     * @return a set of elements with the YOU property.
     */
    public Set<Element> getYouElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.YOU);
    }

    /**
     * Retrieves the set of elements in a cell designated as "JUMP", indicating elements that can jump.
     * 
     * @param cell the cell to check for elements that can jump.
     * @return a set of elements with the JUMP property.
     */
    public Set<Element> getJumpElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.JUMP);
    }

    /**
     * Determines if the specified cell contains an element that can lead to winning the game.
     * 
     * @param cell the cell to check for a winning condition.
     * @return true if the cell contains at least one WIN element, otherwise false.
     */
    public boolean isWin(Cellule cell) {
        return !getWinElements(cell).isEmpty();
    }

    /**
     * Retrieves the current set of transformation rules.
     * 
     * @return a map of noun transformations that dictate changes in element types upon rule activation.
     */
    public Map<Noun, Noun> getTransformationRules() {
        return transformationRules;
    }

    /**
     * Checks if a given set of elements has a specific property.
     * 
     * @param elements the set of elements to check.
     * @param property the property to check for.
     * @return true if any of the elements have the property, false otherwise.
     */
    public boolean hasProperty(Set<Element> elements, Property property) {
        for (Element element : elements) {
            if (nounToProperty.getOrDefault(element, Collections.emptySet()).contains(property)) {
                return true;
            }
        }
        return false;
    }
}
