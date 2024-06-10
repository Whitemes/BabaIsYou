package fr.esiee.baba.model;

import java.util.*;

/**
 * Manages the dynamic effects of rule interactions within the game level, such as melting, defeat, and transformations.
 * This class applies the rules as defined in the current game state to alter the state of the grid elements accordingly.
 */
public class Transmutation {
    private Level level;
    private Rules rules;

    /**
     * Constructs a Transmutation object that manages rule-based transformations within a game level.
     * 
     * @param level the current game level where transformations will be applied.
     * @param rules the set of rules that dictate how elements interact and transform.
     */
    public Transmutation(Level level, Rules rules) {
        this.level = level;
        this.rules = rules;
    }

    /**
     * Applies the melt rule across the game grid. Elements with the MELT property are removed if they share a cell with elements having the HOT property.
     */
    public void checkMelt() {
       var grid = level.getGrid();
        for (var row : grid) {
            for (var cell : row) {
                var meltElements = rules.getEntitiesByProperty(cell, Property.MELT);
                var hotElements = rules.getEntitiesByProperty(cell, Property.HOT);
                if (!meltElements.isEmpty() && !hotElements.isEmpty()) {
                    for (Element meltElement : meltElements) {
                        cell.removeElement(meltElement);
                    }
                }
            }
        }
    }

    /**
     * Applies the defeat rule across the game grid. Elements with the YOU property are removed if they share a cell with elements having the DEFEAT property.
     */
    public void checkDefeat() {
        var grid = level.getGrid();
        for (var row : grid) {
            for (var cell : row) {
                var defeatElements = rules.getEntitiesByProperty(cell, Property.DEFEAT);
                var youElements = rules.getEntitiesByProperty(cell, Property.YOU);
                if (!defeatElements.isEmpty() && !youElements.isEmpty()) {
                    for (Element youElement : youElements) {
                        cell.removeElement(youElement);
                    }
                }
            }
        }
    }

    /**
     * Applies the sink rule across the game grid. Both the element with the SINK property and any other element in the same cell are removed.
     */
    public void checkSink() {
        var grid = level.getGrid();
        for (var row : grid) {
            for (var cell : row) {
                var sinkElements = rules.getEntitiesByProperty(cell, Property.SINK);
                if (!sinkElements.isEmpty() && cell.getElements().size() > 2) {
                    cell.getElements().clear();
                    cell.addElement(Element.EMPTY);
                }
            }
        }
    }
    
    
    

    /**
     * Transforms all entities of the specified source type to the destination type within the entire grid.
     * This method is called to enact transformation rules such as "BABA IS ROCK".
     *
     * @param source the noun representing the original type of entity.
     * @param dest the noun representing the new type of entity after transformation.
     */
    public void applyTransformation(Noun source, Noun dest) {
        var grid = level.getGrid();
        var sourceElement = Rules.getEntityByNoun(source);
        var destElement = Rules.getEntityByNoun(dest);

        for (var row : grid) {
            for (var cell : row) {
                var elementsToTransform = new ArrayList<Element>();
                for (var element : cell.getElements()) {
                    if (element.getEntity() != null && element == sourceElement) {
                        elementsToTransform.add(element);
                    }
                }
                for (var element : elementsToTransform) {
                    cell.removeElement(element);
                    cell.addElement(destElement);
                }
            }
        }
    }

    /**
     * Sets or updates the level and rules associated with this Transmutation.
     * 
     * @param level the new level to be used for transformations.
     * @param rules the new set of rules to apply for element interactions.
     */
    public void setTransmutation(Level level, Rules rules) {
        this.level = level;
        this.rules = rules;
    }
}
