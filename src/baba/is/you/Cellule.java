package baba.is.you;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a cell in a grid. Each cell can hold multiple elements and provides methods for manipulating these elements.
 */
public class Cellule {
    private List<Element> elements;

    /**
     * Constructs an empty Cellule instance.
     */
    public Cellule() {
        this.elements = new ArrayList<>();
    }

    /**
     * Adds an element to the cell.
     * 
     * @param element the element to be added to the cell
     */
    public void addElement(Element element) {
        elements.add(element);
    }

    /**
     * Removes a specified element from the cell.
     * 
     * @param element the element to be removed from the cell
     */
    public void removeElement(Element element) {
        elements.remove(element);
    }

    /**
     * Returns a list of all elements currently in the cell.
     * 
     * @return a list containing the elements in the cell
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Checks if a specific element is contained in the cell.
     * 
     * @param element the element to check for in the cell
     * @return true if the cell contains the element, otherwise false
     */
    public boolean contains(Element element) {
        return elements.contains(element);
    }

    /**
     * Determines whether the cell contains any of the elements in a specified set.
     * 
     * @param elements the set of elements to check against the cell's contents
     * @return true if any element from the set is in the cell, otherwise false
     */
    public boolean containsAny(Set<Element> elements) {
        for (Element element : elements) {
            if (this.elements.contains(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the cell contains the 'IS' operator.
     * 
     * @return true if an 'IS' operator is present in the cell, otherwise false
     */
    public boolean containsOperatorIs() {
        for (var element : elements) {
            if (element.getWord() != null && element.getWord().getOperator() == Operator.IS) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the cell contains a noun.
     * 
     * @return true if a noun is present in the cell, otherwise false
     */
    public boolean hasNoun() {
        for (var element : elements) {
            if (element.getWord() != null && element.getWord().getNoun() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the cell contains a property.
     * 
     * @return true if a property is present in the cell, otherwise false
     */
    public boolean hasProperty() {
        for (var element : elements) {
            if (element.getWord() != null && element.getWord().getProperty() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a noun from the cell, if present.
     * 
     * @return the noun if present, otherwise null
     */
    public Noun getNoun() {
        for (var element : elements) {
            if (element.getWord() != null && element.getWord().getNoun() != null) {

                return element.getWord().getNoun();
            }
        }
        return null;
    }

    /**
     * Retrieves a property from the cell, if present.
     * 
     * @return the property if present, otherwise null
     */
    public Property getProperty() {
        for (var element : elements) {
            if (element.getWord() != null && element.getWord().getProperty() != null) {
                return element.getWord().getProperty();
            }
        }
        return null;
    }

    /**
     * Removes and returns the last element in the cell.
     * 
     * @return the last element removed from the cell, or null if the cell is empty
     */
    public Element removeLastElement() {
        if (!elements.isEmpty()) {
            return elements.remove(elements.size() - 1);
        }
        return null;
    }

    /**
     * Removes elements specified in a set from the cell and returns them in a new cell.
     *
     * @param elementsToPop the set of elements to be removed
     * @return a new cell containing the removed elements
     * @throws NullPointerException if the elementsToPop is null
     */
    public Cellule popElements(Set<Element> elementsToPop) {
        Objects.requireNonNull(elementsToPop, "elementsToPop must not be null");

        var poppedElementsCell = new Cellule();
        var iterator = elements.iterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (elementsToPop.contains(currentElement)) {
                poppedElementsCell.addElement(currentElement);
                iterator.remove();
            }
        }
        return poppedElementsCell;
    }

    /**
     * Determines whether the cell is empty.
     * 
     * @return true if there are no elements in the cell, otherwise false
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public String toString() {
        if (elements.isEmpty()) {
            return "";
        } else {
            return elements.get(elements.size() - 1).toString();
        }
    }
}
