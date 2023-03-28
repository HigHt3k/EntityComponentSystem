package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

public abstract class Component {
    private Entity entity;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {return entity;}
}
