package baba.is.you;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Rules {
    private Level level;
    private Set<String> activeRules;
    private Map<Word.Noun, Word.Property> nounToProperty;

    /**
     * Constructor for Rules class
     * 
     * @param level the current game level
     */
    public Rules(Level level) {
        this.level = level;
        this.activeRules = new HashSet<>();
        this.nounToProperty = new HashMap<>();
        initRules(level);
    }

    /**
     * Initialize rules based on the current level's grid
     * 
     * @param level the current game level
     */
    public void initRules(Level level) {
        activeRules.clear();
        nounToProperty.clear();
        List<List<Element>> grid = level.getGrid();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).size(); j++) {
                if (grid.get(i).get(j).getWord() != null && grid.get(i).get(j).getWord().getOperator() == Word.Operator.IS) {
                    if (i > 0 && i < grid.size() - 1) {
                        // Check vertical rules
                        Element above = grid.get(i - 1).get(j);
                        Element below = grid.get(i + 1).get(j);
                        if (above.getWord() != null && below.getWord() != null) {
                            if (above.getWord().getNoun() != null && below.getWord().getProperty() != null) {
                                activeRules.add(above.getWord().getNoun() + " IS " + below.getWord().getProperty());
                                nounToProperty.put(above.getWord().getNoun(), below.getWord().getProperty());
                            }
                        }
                    }
                    if (j > 0 && j < grid.get(i).size() - 1) {
                        // Check horizontal rules
                        Element left = grid.get(i).get(j - 1);
                        Element right = grid.get(i).get(j + 1);
                        if (left.getWord() != null && right.getWord() != null) {
                            if (left.getWord().getNoun() != null && right.getWord().getProperty() != null) {
                                activeRules.add(left.getWord().getNoun() + " IS " + right.getWord().getProperty());
                                nounToProperty.put(left.getWord().getNoun(), right.getWord().getProperty());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Print the active rules to the console
     */
    public void printRules() {
        System.out.println("Active Rules:");
        for (String rule : activeRules) {
            System.out.println(rule);
        }
    }

    /**
     * Get the set of active rules
     * 
     * @return the set of active rules
     */
    public Set<String> getActiveRules() {
        return activeRules;
    }

    /**
     * Get the set of elements that have the WIN property
     * 
     * @return the set of elements with the WIN property
     */
    public Set<Element> getWinElements() {
        Set<Element> winElements = new HashSet<>();
        for (Map.Entry<Word.Noun, Word.Property> entry : nounToProperty.entrySet()) {
            if (entry.getValue() == Word.Property.WIN) {
                winElements.add(Element.valueOf("ENTITY_" + entry.getKey().name()));
            }
        }
        return winElements;
    }
}
