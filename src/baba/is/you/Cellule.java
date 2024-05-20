package baba.is.you;

import java.util.ArrayList;
import java.util.List;

public class Cellule {
    private List<Element> elements;

    public Cellule() {
        this.elements = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void removeElement(Element element) {
        elements.remove(element);
    }

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
