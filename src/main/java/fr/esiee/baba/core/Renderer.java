package fr.esiee.baba.core;

import fr.esiee.baba.model.Level;

public interface Renderer {
    /**
     * Renders the current state of the level.
     * 
     * @param level the level to render
     */
    void render(Level level);
}
