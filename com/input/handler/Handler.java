package com.input.handler;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Handler {
    private HandlerType handlerType;

    public Handler(HandlerType handlerType) {
        this.handlerType = handlerType;
    }

    public abstract void handle(KeyEvent e);
    public abstract void handle(MouseEvent e);

    public HandlerType getHandlerType() {
        return handlerType;
    }
}
