package com.ecs.input;

import com.Game;
import com.ecs.Entity;
import com.ecs.IntentComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

public class InputManager {
    private final ArrayList<KeyEvent> keyEvents;
    private final ArrayList<MouseEvent> mouseEvents;
    //TODO: implement game pad events and a game pad adapter
    //private ArrayList<GamePadEvent> gamePadEvents;

    public InputManager() {
        keyEvents = new ArrayList<>();
        mouseEvents = new ArrayList<>();
    }

    public void handle() {
        ArrayList<Entity> entities = Game.scene().current().getEntities();

        while(!keyEvents.isEmpty()) {
            KeyEvent e = keyEvents.get(0);
            if(e == null) {
                keyEvents.remove(e);
                continue;
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
}
