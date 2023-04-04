package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system;

public abstract class SystemHandle {
    public boolean needsUpdate = true;
    public abstract void handle();

    public void needsUpdate() {
        this.needsUpdate = true;
    }

}