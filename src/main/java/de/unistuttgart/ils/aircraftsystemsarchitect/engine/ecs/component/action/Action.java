package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

/**
 * abstract class to implement actions which are handled by the game engines' {@link de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system.ActionSystem}.
 * actions must implement a handle method, which defines the behavior after calling an action (which may be done for example by buttons).
 */
public abstract class Action {
    /**
     * Defines how to handle the action.
     */
    public abstract void handle();
}
