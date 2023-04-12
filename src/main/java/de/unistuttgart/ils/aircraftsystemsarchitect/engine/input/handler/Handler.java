package de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Handler {
    private final HandlerType handlerType;
    private boolean active = true;

    public Handler(HandlerType handlerType) {
        this.handlerType = handlerType;
    }

    public abstract void handle(KeyEvent e);

    public abstract void handle(MouseEvent e);

    public abstract void handle(InputAction e);

    public HandlerType getHandlerType() {
        return handlerType;
    }

    /**
     * Get the activity state of the cursor
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the activity state of the cursor
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
