package com.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

public abstract class InputManager {
    private final ArrayList<KeyEvent> keyEvents;
    private final ArrayList<MouseEvent> mouseEvents;
    //private ArrayList<GamePadEvent> gamePadEvents;

    public InputManager() {
        keyEvents = new ArrayList<>();
        mouseEvents = new ArrayList<>();
    }

    public abstract void handle();

    public void queueEvent(MouseEvent e) {
        mouseEvents.add(e);
    }

    public void queueEvent(KeyEvent e) {
        keyEvents.add(e);
    }

    protected ArrayList<MouseEvent> getMouseEvents() {
        return mouseEvents;
    }

    protected ArrayList<KeyEvent> getKeyEvents() {
        return keyEvents;
    }
}
