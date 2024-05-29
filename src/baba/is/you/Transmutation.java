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
        List<List<Cellule>> grid = level.getGrid();
        for (List<Cellule> row : grid) {
            for (Cellule cell : row) {
                Set<Element> meltElements = rules.getEntitiesByProperty(cell, Word.Property.MELT);
                Set<Element> hotElements = rules.getEntitiesByProperty(cell, Word.Property.HOT);
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
        List<List<Cellule>> grid = level.getGrid();
        for (List<Cellule> row : grid) {
            for (Cellule cell : row) {
                Set<Element> defeatElements = rules.getEntitiesByProperty(cell, Word.Property.DEFEAT);
                Set<Element> youElements = rules.getEntitiesByProperty(cell, Word.Property.YOU);
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
        List<List<Cellule>> grid = level.getGrid();
        for (List<Cellule> row : grid) {
            for (Cellule cell : row) {
                Set<Element> sinkElements = rules.getEntitiesByProperty(cell, Word.Property.SINK);
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
    public void applyTransformation(Word.Noun source, Word.Noun dest) {
        List<List<Cellule>> grid = level.getGrid();
        Element sourceElement = Rules.getEntityByNoun(source);
        Element destElement = Rules.getEntityByNoun(dest);

        for (List<Cellule> row : grid) {
            for (Cellule cell : row) {
                List<Element> elementsToTransform = new ArrayList<>();
                for (Element element : cell.getElements()) {
                    if (element.getEntity() != null && element == sourceElement) {
                        elementsToTransform.add(element);
                    }
                }
                for (Element element : elementsToTransform) {
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
