package baba.is.you;

import java.util.*;

public class Transmutation {
    private Level level;
    private Rules rules;

    /**
     * Constructor for Transmutation class
     * 
     * @param level the current game level
     * @param rules the current game rules
     */
    public Transmutation(Level level, Rules rules) {
        this.level = level;
        this.rules = rules;
    }

    /**
     * Check and apply melt rules to the grid
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
     * Check and apply defeat rules to the grid
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
     * Check and apply sink rules to the grid
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
     * Transform all entities of the source type to the destination type in the grid.
     *
     * @param source the source noun
     * @param dest the destination noun
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

    public void setTransmutation(Level level, Rules rules) {
        this.level = level;
        this.rules = rules;
    }
}
