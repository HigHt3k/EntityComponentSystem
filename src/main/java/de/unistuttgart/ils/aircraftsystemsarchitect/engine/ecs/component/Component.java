package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

/**
 * Abstract Component class. Used to inherit new components, which are basically data containers that describe behavior of an
 * object with the game engines entity component system.
 */
public abstract class Component {
    private Entity entity;

    /**
     * Set the parent entity of the component
     * @param entity: parent entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Get the parent entity
     * @return parent entity
     */
    public Entity getEntity() {return entity;}
}
