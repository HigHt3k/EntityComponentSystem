package com.ecs.entity;

import com.Game;
import com.IdGenerator;
import com.ecs.component.Component;
import com.ecs.component.IntentComponent;
import com.ecs.intent.DebugIntent;

import java.util.ArrayList;


public class Entity implements Cloneable {

    private ArrayList<Component> components;
    private String name;
    private int id;
    private boolean removable = true;

    public Entity(String name, int id) {
        this.name = name;
        this.id = id;
        components = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for(Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch(ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(int i = 0; i < components.size(); i ++) {
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        this.components.add(c);
        c.setEntity(this);
    }

    public void update() {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).update();
        }
    }

    public void start() {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
