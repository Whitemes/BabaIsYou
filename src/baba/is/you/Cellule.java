package baba.is.you;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Cellule {
    private List<Element> elements;

    /**
     * Constructor for Cellule class.
     */
    public Cellule() {
        this.elements = new ArrayList<>();
    }

    /**
     * Add an element to the cell.
     * 
     * @param element the element to add
     */
    public void addElement(Element element) {
        elements.add(element);
    }

    /**
     * Remove an element from the cell.
     * 
     * @param element the element to remove
     */
    public void removeElement(Element element) {
        elements.remove(element);
    }

    /**
     * Get the list of elements in the cell.
     * 
     * @return the list of elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Check if the cell contains a specific element.
     * 
     * @param element the element to check for
     * @return true if the cell contains the element, false otherwise
     */
    public boolean contains(Element element) {
        return elements.contains(element);
    }

    /**
     * Check if the cell contains any element from a set of elements.
     * 
     * @param elements the set of elements to check for
     * @return true if the cell contains any element from the set, false otherwise
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
     * Check if the cell contains an operator 'IS'.
     * 
     * @return true if the cell contains an 'IS' operator, false otherwise
     */
    public boolean containsOperatorIs() {
        for (Element element : elements) {
            if (element.getWord() != null && element.getWord().getOperator() == Word.Operator.IS) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the cell contains a noun.
     * 
     * @return true if the cell contains a noun, false otherwise
     */
    public boolean hasNoun() {
        for (Element element : elements) {
            if (element.getWord() != null && element.getWord().getNoun() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the cell contains a property.
     * 
     * @return true if the cell contains a property, false otherwise
     */
    public boolean hasProperty() {
        for (Element element : elements) {
            if (element.getWord() != null && element.getWord().getProperty() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the noun from the cell.
     * 
     * @return the noun if present, otherwise null
     */
    public Word.Noun getNoun() {
        for (Element element : elements) {
            if (element.getWord() != null && element.getWord().getNoun() != null) {
                return element.getWord().getNoun();
            }
        }
        return null;
    }

    /**
     * Get the property from the cell.
     * 
     * @return the property if present, otherwise null
     */
    public Word.Property getProperty() {
        for (Element element : elements) {
            if (element.getWord() != null && element.getWord().getProperty() != null) {
                return element.getWord().getProperty();
            }
        }
        return null;
    }

    /**
     * Remove the last element from the cell and return it.
     * 
     * @return the removed element
     */
    public Element removeLastElement() {
        if (!elements.isEmpty()) {
            return elements.remove(elements.size() - 1);
        }
        return null;
    }

    /**
     * Remove elements that are present in the specified set from the cell and return them in a new cell.
     *
     * @param elementsToPop the set of elements to remove
     * @return a new cell containing the removed elements
     * @throws NullPointerException if elementsToPop is null
     */
    public Cellule popElements(Set<Element> elementsToPop) {
        Objects.requireNonNull(elementsToPop, "elementsToPop must not be null");

        Cellule poppedElementsCell = new Cellule();
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element currentElement = iterator.next();
            if (elementsToPop.contains(currentElement)) {
                poppedElementsCell.addElement(currentElement);
                iterator.remove();
            }
        }
        return poppedElementsCell;
    }

    /**
     * Check if the cell is empty.
     * 
     * @return true if the cell is empty, false otherwise
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
