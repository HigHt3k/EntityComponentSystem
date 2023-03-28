package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.util.ArrayList;

public abstract class Scene {
    private final String name;
    private final int id;
    private final ArrayList<Entity> entities;
    private final boolean isRunning = false;

    public Scene(String name, int id) {
        this.name = name;
        this.id = id;

        entities = new ArrayList<>();
    }

    public Entity getEntityById(int id) {
        for(Entity e : getEntities()) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public abstract void init();


    public void addEntityToScene(Entity e) {
        entities.add(e);
    }

    public void removeEntityFromScene(Entity e) {
        entities.remove(e);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Entity getEntityByName(String name) {
        for(Entity e : getEntities()) {
            if(e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }
}
