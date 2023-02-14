package engine.graphics.scene;

import engine.ecs.entity.Entity;

import java.util.ArrayList;

public abstract class Scene {
    private String name;
    private int id;
    private ArrayList<Entity> entities;
    private boolean isRunning = false;

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

    public void start() {
        for(Entity e : entities) {
            e.start();
        }
    }

    public void addEntityToScene(Entity e) {
        entities.add(e);
        if(isRunning) {
            e.start();
        }
    }

    public void removeEntityFromScene(Entity e) {
        entities.remove(e);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public abstract void update();

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
