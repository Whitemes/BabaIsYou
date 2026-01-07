package fr.esiee.baba.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class RulesTest {

    @Test
    void testBasicRuleParsing() {
        // Setup a mini grid: BABA IS YOU
        // Row 0: BABA (Noun), IS (Op), YOU (Prop)
        List<List<Cellule>> grid = new ArrayList<>();
        List<Cellule> row = new ArrayList<>();

        Cellule c1 = new Cellule();
        c1.addElement(Element.BABA);
        row.add(c1);
        Cellule c2 = new Cellule();
        c2.addElement(Element.IS);
        row.add(c2);
        Cellule c3 = new Cellule();
        c3.addElement(Element.YOU);
        row.add(c3);

        grid.add(row);

        // Mock Level (needs dummy path)
        Level level = new Level(grid, "dummy.txt");
        Rules rules = new Rules(level);

        // Verify property mapping
        assertTrue(rules.hasProperty(Set.of(Element.ENTITY_BABA), Property.YOU),
                "BABA entity should have YOU property");
    }
}
