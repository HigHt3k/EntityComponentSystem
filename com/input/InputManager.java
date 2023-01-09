package com.input;

import com.Game;
import com.ecs.entity.Entity;
import com.ecs.component.IntentComponent;
import com.input.handler.Handler;

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


            for(Handler h : handlers) {
                h.handle(e);
            }

            // send key event to all components with intents and check wether the intent is applicable
            for(Entity entity : entities) {
                IntentComponent ic = entity.getComponent(IntentComponent.class);
                if(ic != null) {
                    ic.handle(e);
                }
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
                h.handle(e);
            }

            // send mouse event to all components with intents and check wether the intent is applicable
            for(Entity entity : entities) {
                IntentComponent ic = entity.getComponent(IntentComponent.class);
                if(ic != null) {
                    ic.handle(e);
                }
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
}
