package fr.esiee.baba.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class LevelTest {

    @Test
    void testMovement() {
        // Setup: [BABA] [EMPTY]
        List<List<Cellule>> grid = new ArrayList<>();
        List<Cellule> row = new ArrayList<>();

        Cellule c1 = new Cellule();
        c1.addElement(Element.ENTITY_BABA);
        row.add(c1);
        Cellule c2 = new Cellule();
        row.add(c2);

        grid.add(row);

        Level level = new Level(grid, "test.txt");
        Rules rules = new Rules(level);

        // Define Rule: BABA IS YOU
        // We simulate this by mocking the property map or adding rule manually if
        // possible?
        // Rules parses grid. So we need the rule in the grid OR we can inject the rule.
        // Rules.java is tied to grid scanning. So we must put BABA IS YOU in grid.
    }

    @Test
    void testGridParsingWithRules() {
        // | BABA | IS | YOU |
        // | | | |
        // | BABA | . | . |

        List<List<Cellule>> grid = new ArrayList<>();
        // Row 0: Rules
        List<Cellule> r0 = new ArrayList<>();
        r0.add(createCell(Element.BABA));
        r0.add(createCell(Element.IS));
        r0.add(createCell(Element.YOU));
        grid.add(r0);

        // Row 1: Entity
        List<Cellule> r1 = new ArrayList<>();
        r1.add(createCell(Element.ENTITY_BABA));
        r1.add(createCell(Element.EMPTY));
        r1.add(createCell(Element.EMPTY));
        grid.add(r1);

        Level level = new Level(grid, "test_rules.txt");
        Rules rules = new Rules(level);

        // Initial state: Rules should be active
        assertTrue(rules.hasProperty(level.getYouElements(), Property.YOU));

        // Move Right
        level.update(Direction.RIGHT, false);

        // Check Baba moved
        // Grid[1][0] should be empty, Grid[1][1] should have BABA
        assertTrue(level.getGrid().get(1).get(0).isEmpty()
                || level.getGrid().get(1).get(0).getElements().contains(Element.EMPTY));
        // Note: Cellule might have EMPTY element explicitly.

        assertTrue(level.getGrid().get(1).get(1).contains(Element.ENTITY_BABA));
    }

    private Cellule createCell(Element e) {
        Cellule c = new Cellule();
        if (e != null)
            c.addElement(e);
        else
            c.addElement(Element.EMPTY);
        return c;
    }
}
