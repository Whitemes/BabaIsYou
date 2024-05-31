package baba.is.you;

import java.util.*;

public class Rules {
    //private Level level;
    private Map<Element, Set<Property>> nounToProperty;
    private Map<Property, Set<Element>> propertyToNoun;
    private Map<Noun, Noun> transformationRules;

    /**
     * Constructor for Rules class
     * 
     * @param level the current game level
     */
    public Rules(Level level) {
        //this.level = level;
        this.nounToProperty = new HashMap<>();
        this.propertyToNoun = new HashMap<>();
        this.transformationRules = new HashMap<>();
        initRules(level);
    }

    /**
     * Initialize rules based on the current level grid
     * 
     * @param level the current game level
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

    private boolean isValidRule(Cellule first, Cellule second) {
        return first.hasNoun() && (second.hasProperty() || second.hasNoun());
    }

    private void addRule(Element noun, Property property) {
        // Add to nounToProperty map
        nounToProperty.computeIfAbsent(noun, k -> new HashSet<>()).add(property);

        // Add to propertyToNoun map
        propertyToNoun.computeIfAbsent(property, k -> new HashSet<>()).add(noun);
    }

    private void addTransformationRule(Noun firstNoun, Noun secondNoun) {
        transformationRules.put(firstNoun, secondNoun);
    }

    private boolean isOperatorIs(List<List<Cellule>> grid, int i, int j) {
        return grid.get(i).get(j).containsOperatorIs();
    }

    /**
     * Print nounToProperty and propertyToNoun maps
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
     * Get the entity corresponding to a noun
     * 
     * @param noun the noun to get the entity for
     * @return the corresponding entity
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
            default:
                throw new IllegalArgumentException("Unknown noun: " + noun);
        }
    }

    /**
     * Get entities by property in a given cell
     * 
     * @param cell the cell to check
     * @param property the property to check
     * @return the set of entities with the specified property
     */
    Set<Element> getEntitiesByProperty(Cellule cell, Property property) {
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
     * Get win elements in a given cell
     * 
     * @param cell the cell to check
     * @return the set of elements with the WIN property
     */
    public Set<Element> getWinElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.WIN);
    }

    /**
     * Get pushable elements in a given cell
     * 
     * @param cell the cell to check
     * @return the set of pushable elements
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
     * Get stop elements in a given cell
     * 
     * @param cell the cell to check
     * @return the set of elements with the STOP property
     */
    public Set<Element> getStopElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.STOP);
    }

    /**
     * Get BABA elements in a given cell
     * 
     * @param cell the cell to check
     * @return the set of elements with the BABA property
     */
    public Set<Element> getBabaElements(Cellule cell) {
        return getEntitiesByProperty(cell, Property.YOU);
    }

    /**
     * Check if a cell contains a WIN element
     * 
     * @param cell the cell to check
     * @return true if the cell contains a WIN element, false otherwise
     */
    public boolean isWin(Cellule cell) {
        return !getWinElements(cell).isEmpty();
    }

    /**
     * Get transformation rules
     * 
     * @return the transformation rules
     */
    public Map<Noun, Noun> getTransformationRules() {
        return transformationRules;
    }
}
