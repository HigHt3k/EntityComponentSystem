package engine.input;

import engine.Game;
import engine.ecs.entity.Entity;
import engine.ecs.component.IntentComponent;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

public class InputManager {
    private final ArrayList<KeyEvent> keyEvents;
    private final ArrayList<MouseEvent> mouseEvents;

    private ArrayList<Handler> handlers;
    //TODO: implement game pad events and a game pad adapter
    //private ArrayList<GamePadEvent> gamePadEvents;

    public InputManager() {
        keyEvents = new ArrayList<>();
        mouseEvents = new ArrayList<>();
        handlers = new ArrayList<>();
    }

    public void handle() {
        ArrayList<Entity> entities = (ArrayList<Entity>) Game.scene().current().getEntities().clone();

        while(!keyEvents.isEmpty()) {
            KeyEvent e = keyEvents.get(0);
            if(e == null) {
                keyEvents.remove(e);
                continue;
            }

            for(Handler h : (ArrayList<Handler>) handlers.clone()) {
                if(h.getHandlerType() == HandlerType.EVENT)
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
                } catch(ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
