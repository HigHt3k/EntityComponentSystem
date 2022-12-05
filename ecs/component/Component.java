package ecs.component;

import ecs.entity.Entity;

public abstract class Component {
    private Entity entity;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public abstract void update(float dTime);

}
