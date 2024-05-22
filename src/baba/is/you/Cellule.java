package baba.is.you;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a cell in the game grid
 */
public class Cellule {
    private List<Element> elements;

    /**
     * Constructor for Cellule
     */
    public Cellule() {
        this.elements = new ArrayList<>();
    }

    /**
     * Get the list of elements in the cell
     * 
     * @return the list of elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Add an element to the cell
     * 
     * @param element the element to add
     */
    public void addElement(Element element) {
        elements.add(element);
    }

    /**
     * Remove an element from the cell
     * 
     * @param element the element to remove
     */
    public void removeElement(Element element) {
        elements.remove(element);
    }

    /**
     * Check if the cell contains a specific element
     * 
     * @param element the element to check
     * @return true if the element is present, false otherwise
     */
    public boolean contains(Element element) {
        return elements.contains(element);
    }

    @Override
    public String toString() {
        // Prioritize the display of certain elements
        if (contains(Element.ENTITY_BABA)) {
            return Element.ENTITY_BABA.toString();
        } else if (contains(Element.ENTITY_FLAG)) {
            return Element.ENTITY_FLAG.toString();
        } else if (contains(Element.ENTITY_ROCK)) {
            return Element.ENTITY_ROCK.toString();
        } else if (contains(Element.EMPTY)) {
            return Element.EMPTY.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            for (Element element : elements) {
                sb.append(element.toString()).append(" ");
            }
            return sb.toString().trim();
        }
    }
}
