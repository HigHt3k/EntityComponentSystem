package com.graphics.scene;

import com.ecs.Entity;

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
}
