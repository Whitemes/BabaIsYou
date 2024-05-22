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
                if (isOperatorIs(grid, i, j)) {
                    checkVerticalRules(grid, i, j);
                    checkHorizontalRules(grid, i, j);
                }
            }
        }
    }

    /**
     * Check for vertical rules at the given position in the grid
     *
     * @param grid the grid of elements
     * @param i    the row index
     * @param j    the column index
     */
    private void checkVerticalRules(List<List<Element>> grid, int i, int j) {
        if (i > 0 && i < grid.size() - 1) {
            Element above = grid.get(i - 1).get(j);
            Element below = grid.get(i + 1).get(j);
            if (isValidRule(above, below)) {
                addRule(above.getWord().getNoun(), below.getWord().getProperty());
            }
        }
    }

    /**
     * Check for horizontal rules at the given position in the grid
     *
     * @param grid the grid of elements
     * @param i    the row index
     * @param j    the column index
     */
    private void checkHorizontalRules(List<List<Element>> grid, int i, int j) {
        if (j > 0 && j < grid.get(i).size() - 1) {
            Element left = grid.get(i).get(j - 1);
            Element right = grid.get(i).get(j + 1);
            if (isValidRule(left, right)) {
                addRule(left.getWord().getNoun(), right.getWord().getProperty());
            }
        }
    }

    /**
     * Determine if the given elements form a valid rule
     *
     * @param first  the first element
     * @param second the second element
     * @return true if the elements form a valid rule, false otherwise
     */
    private boolean isValidRule(Element first, Element second) {
        return first.getWord() != null && second.getWord() != null
                && first.getWord().getNoun() != null && second.getWord().getProperty() != null;
    }

    /**
     * Add a rule to the active rules set and the noun to property map
     *
     * @param noun     the noun in the rule
     * @param property the property in the rule
     */
    private void addRule(Word.Noun noun, Word.Property property) {
        activeRules.add(noun + " IS " + property);
        nounToProperty.put(noun, property);
    }

    /**
     * Check if the element at the given position is an 'IS' operator
     *
     * @param grid the grid of elements
     * @param i    the row index
     * @param j    the column index
     * @return true if the element is an 'IS' operator, false otherwise
     */
    private boolean isOperatorIs(List<List<Element>> grid, int i, int j) {
        return grid.get(i).get(j).getWord() != null && grid.get(i).get(j).getWord().getOperator() == Word.Operator.IS;
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
