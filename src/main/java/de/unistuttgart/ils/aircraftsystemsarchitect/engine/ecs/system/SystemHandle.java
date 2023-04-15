package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system;

/**
 * Abstract class representing a system handle within an aircraft system architecture.
 * A system handle manages whether the associated system needs an update.
 * Subclasses must implement the handle method for the specific handling logic.
 */
public abstract class SystemHandle {

    /**
     * Flag to indicate whether the system needs to be updated.
     * Initially set to true, as it assumes the system needs to be updated.
     */
    public boolean needsUpdate = true;

    /**
     * Abstract method to handle the system updates.
     * This method must be implemented by subclasses to provide
     * the specific handling logic for the associated system.
     */
    public abstract void handle();

    /**
     * Sets the needsUpdate flag to true, indicating that the system requires an update.
     */
    public void needsUpdate() {
        this.needsUpdate = true;
    }
}
