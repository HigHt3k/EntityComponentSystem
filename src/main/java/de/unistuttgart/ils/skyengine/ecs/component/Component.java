package de.unistuttgart.ils.skyengine.ecs.component;

import de.unistuttgart.ils.skyengine.ecs.entity.Entity;

public abstract class Component {
    private Entity entity;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public abstract void update();

    public void start() {

    }

    public Entity getEntity() {return entity;}

}
