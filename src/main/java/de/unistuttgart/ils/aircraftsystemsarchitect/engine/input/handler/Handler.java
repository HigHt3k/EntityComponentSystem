package de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Handler {
    private final HandlerType handlerType;

    public Handler(HandlerType handlerType) {
        this.handlerType = handlerType;
    }

    public abstract void handle(KeyEvent e);

    public abstract void handle(MouseEvent e);

    public abstract void handle(InputAction e);

    public HandlerType getHandlerType() {
        return handlerType;
    }
}
