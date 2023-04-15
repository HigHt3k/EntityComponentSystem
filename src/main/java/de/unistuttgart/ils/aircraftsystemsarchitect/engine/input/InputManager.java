package de.unistuttgart.ils.aircraftsystemsarchitect.engine.input;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.GamePadAdapter;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.Handler;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Class responsible for managing input events from various sources, such as keyboard, mouse, and gamepads.
 */
public class InputManager {
    private final GamePadAdapter gamePadAdapter;
    private final ArrayList<KeyEvent> keyEvents;
    private final ArrayList<MouseEvent> mouseEvents;
    private final ArrayList<InputAction> gamePadEvents;

    private long keyboardInactiveTime = 0;
    private long gamepadInactiveTime = 0;

    private ArrayList<Handler> handlers;

    /**
     * Constructor for the InputManager class, initializing necessary data structures.
     */
    public InputManager() {
        gamePadAdapter = new GamePadAdapter();
        keyEvents = new ArrayList<>();
        mouseEvents = new ArrayList<>();
        gamePadEvents = new ArrayList<>();
        handlers = new ArrayList<>();
    }

    /**
     * Handles inputs from mouse, keyboard, and controllers in a synchronized manner.
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
                    h.setActive(false);
                }
            }
        } else {
            for (Handler h : (ArrayList<Handler>) handlers.clone()) {
                if (h.getHandlerType() == HandlerType.EVENT) {
                    h.setActive(true);
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

            if(Game.config().isDebug()) {
                if(!Game.input().getMouseEvents().isEmpty()) {
                    Game.graphics().setCursorPosition(e.getX(), e.getY());
                }
            }

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

    /**
     * Queues a MouseEvent to be processed.
     *
     * @param e The MouseEvent to be queued.
     */
    public synchronized void queueEvent(MouseEvent e) {
        mouseEvents.add(e);
    }

    /**
     * Queues a KeyEvent to be processed.
     *
     * @param e The KeyEvent to be queued.
     */
    public synchronized void queueEvent(KeyEvent e) {
        keyEvents.add(e);
    }

    /**
     * Returns a list of MouseEvents.
     *
     * @return An ArrayList of MouseEvents.
     */
    public ArrayList<MouseEvent> getMouseEvents() {
        return mouseEvents;
    }

    /**
     * Returns a list of KeyEvents.
     *
     * @return An ArrayList of KeyEvents.
     */
    public ArrayList<KeyEvent> getKeyEvents() {
        return keyEvents;
    }

    /**
     * Adds a handler to the list of handlers.
     *
     * @param h The Handler to be added.
     */
    public void addHandler(Handler h) {
        this.handlers.add(h);
    }

    /**
     * Removes all handlers from the list.
     */
    public void removeAllHandlers() {
        this.handlers = new ArrayList<>();
    }

    /**
     * Returns a list of handlers.
     *
     * @return An ArrayList of Handlers.
     */
    public ArrayList<Handler> getHandlers() {
        return handlers;
    }

    /**
     * Retrieves a handler of a specific type from the list of handlers.
     *
     * @param componentClass The Class object of the handler type to retrieve.
     * @return The found handler instance or null if not found.
     */
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

    /**
     * Queues an InputAction event to be processed.
     *
     * @param e The InputAction event to be queued.
     */
    public synchronized void queueEvent(InputAction e) {
        gamePadEvents.add(e);
    }

    /**
     * Removes a handler of a specific type from the list of handlers.
     *
     * @param handlerClass The Class object of the handler type to remove.
     */
    public synchronized <T extends Handler> void removeHandler(Class<T> handlerClass) {
        handlers.removeIf(h -> handlerClass.isAssignableFrom(h.getClass()));
    }
}
