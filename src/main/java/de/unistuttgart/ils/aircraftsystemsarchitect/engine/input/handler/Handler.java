package de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Abstract class representing a handler for different input events.
 */
public abstract class Handler {
    private final HandlerType handlerType;
    private boolean active = true;

    /**
     * Constructor for the Handler class.
     *
     * @param handlerType The type of handler to be created.
     */
    public Handler(HandlerType handlerType) {
        this.handlerType = handlerType;
    }

    /**
     * Handles the KeyEvent input.
     *
     * @param e The KeyEvent to be processed.
     */
    public abstract void handle(KeyEvent e);

    /**
     * Handles the MouseEvent input.
     *
     * @param e The MouseEvent to be processed.
     */
    public abstract void handle(MouseEvent e);

    /**
     * Handles the InputAction input.
     *
     * @param e The InputAction to be processed.
     */
    public abstract void handle(InputAction e);

    /**
     * Returns the type of handler.
     *
     * @return The handler type.
     */
    public HandlerType getHandlerType() {
        return handlerType;
    }

    /**
     * Gets the activity state of the handler.
     *
     * @return True if the handler is active, otherwise false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the activity state of the handler.
     *
     * @param active True if the handler should be active, otherwise false.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
