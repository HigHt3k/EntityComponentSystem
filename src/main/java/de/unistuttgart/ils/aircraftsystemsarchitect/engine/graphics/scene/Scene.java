package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.util.ArrayList;

/**
 * abstract Scene class, which can be extended to create scenes (collections of entities) for an application
 */
public abstract class Scene {
    private final String name;
    private final int id;
    private final ArrayList<Entity> entities;
    private String scenePath;

    /**
     * Create a new scene object
     * @param name: name of the scene
     * @param id: id of the scene (should be unique) TODO: Add non-unique-scene warning
     */
    public Scene(String name, int id) {
        this.name = name;
        this.id = id;

        entities = new ArrayList<>();
    }

    /**
     * Find an entity by the entity id
     * @param id: the id of the entity
     * @return the entity if available in this scene
     */
    public Entity getEntityById(int id) {
        for(Entity e : getEntities()) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Initialize the scene
     */
    public abstract void init();


    /**
     * Add an entity to the scene
     * @param e: the entity to add
     */
    public void addEntityToScene(Entity e) {
        entities.add(e);
    }

    /**
     * Remove entity from scene
     * @param e: the entity to delete
     */
    public void removeEntityFromScene(Entity e) {
        entities.remove(e);
    }

    /**
     * Get a list of all entities available in the scene
     * @return the list of entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Get the name of the scene
     * @return scene name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the id of the scene
     * @return scene id
     */
    public int getId() {
        return id;
    }

    /**
     * Get an entity by its name
     * @param name: the entity name
     * @return the entity, if available in the scene
     */
    public Entity getEntityByName(String name) {
        for(Entity e : getEntities()) {
            if(e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public void setScenePath(String scenePath) {
        this.scenePath = scenePath;
    }

    public String getScenePath() {
        return scenePath;
    }
}
