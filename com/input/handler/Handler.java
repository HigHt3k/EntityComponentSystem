package com.input.handler;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class Handler {

    public abstract void handle(KeyEvent e);
    public abstract void handle(MouseEvent e);
}
