package game.scenes.helpers;

import engine.ecs.entity.Entity;

import java.util.ArrayList;

public class BuildPanelPage {
    private final int pageId;
    private final ArrayList<Entity> entitiesInPage;

    public BuildPanelPage(int pageId) {
        this.pageId = pageId;
        this.entitiesInPage = new ArrayList<>();
    }

    public void addToPage(Entity e) {
        entitiesInPage.add(e);
    }

    public ArrayList<Entity> getEntitiesInPage() {
        return entitiesInPage;
    }

    public int getPageId() {
        return pageId;
    }
}
