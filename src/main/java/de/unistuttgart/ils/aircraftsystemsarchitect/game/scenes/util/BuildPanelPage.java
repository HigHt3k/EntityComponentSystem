package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.util.ArrayList;

/**
 * A class representing a single page in the BuildPanel.
 */
public class BuildPanelPage {
    private final int pageId;
    private final ArrayList<Entity> entitiesInPage;

    /**
     * Constructs a new BuildPanelPage object.
     *
     * @param pageId The ID of the page.
     */
    public BuildPanelPage(int pageId) {
        this.pageId = pageId;
        this.entitiesInPage = new ArrayList<>();
    }

    /**
     * Adds an entity to the page.
     *
     * @param e The entity to add.
     */
    public void addToPage(Entity e) {
        entitiesInPage.add(e);
    }

    /**
     * Returns a list of entities in the page.
     *
     * @return A list of entities.
     */
    public ArrayList<Entity> getEntitiesInPage() {
        return entitiesInPage;
    }

    /**
     * Returns the ID of the page.
     *
     * @return The page ID.
     */
    public int getPageId() {
        return pageId;
    }
}
