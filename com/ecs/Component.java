package com.ecs;

public abstract class Component {
    private Entity entity;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public abstract void update(float dTime);

    public void start() {

    }

}
