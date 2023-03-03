package engine.input;

import com.badlogic.gdx.Input;
import engine.Game;
import engine.ecs.entity.Entity;
import engine.input.gamepad.GamePadAdapter;
import engine.input.gamepad.InputAction;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

public class InputManager {
    private final GamePadAdapter gamePadAdapter;
    private final ArrayList<KeyEvent> keyEvents;
    private final ArrayList<MouseEvent> mouseEvents;
    private ArrayList<InputAction> gamePadEvents;

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
    public void handle() {
        collectGamePadEvents();
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

            for(Handler h : handlers) {
                if(h.getHandlerType() == HandlerType.EVENT)
                    h.handle(e);
            }

            mouseEvents.remove(e);
        }
    }

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

    public void queueEvent(InputAction e) {
        gamePadEvents.add(e);
    }

    private void collectGamePadEvents() {
        if (gamePadAdapter.isConnected()) {
            gamePadEvents.addAll(gamePadAdapter.actions());
        }
    }
}