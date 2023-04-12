package de.unistuttgart.ils.aircraftsystemsarchitect.engine.input;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.GamePadAdapter;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.Handler;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

public class InputManager {
    private final GamePadAdapter gamePadAdapter;
    private final ArrayList<KeyEvent> keyEvents;
    private final ArrayList<MouseEvent> mouseEvents;
    private final ArrayList<InputAction> gamePadEvents;

    private long keyboardInactiveTime = 0;
    private long gamepadInactiveTime = 0;

    private ArrayList<Handler> handlers;

    public InputManager() {
        gamePadAdapter = new GamePadAdapter();
        keyEvents = new ArrayList<>();
        mouseEvents = new ArrayList<>();
        gamePadEvents = new ArrayList<>();
        handlers = new ArrayList<>();
    }

    /**
     * Handle inputs from mouse, keyboard and controllers
     */
    public synchronized void handle() {
        // make set of game pad events, only handle each input action once per frame, there may be more due to another thread collecting the events.
        HashSet hs = new HashSet(gamePadEvents);
        gamePadEvents.clear();
        gamePadEvents.addAll(hs);

        if(gamePadEvents.isEmpty()) {
            gamepadInactiveTime += Game.config().getDefaultTickRate();
        } else {
            gamepadInactiveTime = 0;
        }

        while (!gamePadEvents.isEmpty()) {
            InputAction e = gamePadEvents.get(0);
            if (e == null) {
                gamePadEvents.remove(e);
                continue;
            }

            for (Handler h : (ArrayList<Handler>) handlers.clone()) {
                if (h.getHandlerType() == HandlerType.EVENT) {
                    h.handle(e);
                }
            }

            gamePadEvents.remove(e);
        }

        if(keyEvents.isEmpty()) {
            keyboardInactiveTime += Game.config().getDefaultTickRate();
        } else {
            keyboardInactiveTime = 0;
        }

        if(keyboardInactiveTime > 5000 && gamepadInactiveTime > 5000) {
            for (Handler h : (ArrayList<Handler>) handlers.clone()) {
                if (h.getHandlerType() == HandlerType.EVENT) {
                    if(h.isActive()) h.setActive(false);
                }
            }
        } else {
            for (Handler h : (ArrayList<Handler>) handlers.clone()) {
                if (h.getHandlerType() == HandlerType.EVENT) {
                    if(!h.isActive()) h.setActive(true);
                }
            }
        }

        while (!keyEvents.isEmpty()) {
            KeyEvent e = keyEvents.get(0);
            if (e == null) {
                keyEvents.remove(e);
                continue;
            }

            for (Handler h : (ArrayList<Handler>) handlers.clone()) {
                if (h.getHandlerType() == HandlerType.EVENT)
                    h.handle(e);
            }

            keyEvents.remove(e);
        }

        while(!mouseEvents.isEmpty()) {
            MouseEvent e = mouseEvents.get(0);
            if(e == null) {
                mouseEvents.remove(e);
                continue;
            }

            for(Handler h : (ArrayList<Handler>) handlers.clone()) {
                if(h.getHandlerType() == HandlerType.EVENT)
                    h.handle(e);
            }

            mouseEvents.remove(e);
        }
    }

    public synchronized void queueEvent(MouseEvent e) {
        mouseEvents.add(e);
    }

    public synchronized void queueEvent(KeyEvent e) {
        keyEvents.add(e);
    }

    protected ArrayList<MouseEvent> getMouseEvents() {
        return mouseEvents;
    }

    protected ArrayList<KeyEvent> getKeyEvents() {
        return keyEvents;
    }

    public void addHandler(Handler h) {
        this.handlers.add(h);
    }

    public void removeAllHandlers() {
        this.handlers = new ArrayList<>();
    }

    public ArrayList<Handler> getHandlers() {
        return handlers;
    }

    public <T extends Handler> T getHandler(Class<T> componentClass) {
        for(Handler h : handlers) {
            if(componentClass.isAssignableFrom(h.getClass())) {
                try {
                    return componentClass.cast(h);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public synchronized void queueEvent(InputAction e) {
        gamePadEvents.add(e);
    }

    public synchronized <T extends Handler> void removeHandler(Class<T> handlerClass) {
        handlers.removeIf(h -> handlerClass.isAssignableFrom(h.getClass()));
    }
}
