package ecs.entity;

import ecs.component.Component;

import java.util.ArrayList;

public class Entity {

    private ArrayList<Component> components;

    public Entity() {
        components = new ArrayList<>();
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

    public void update(float dTime) {
        for(int i = 0; i < components.size(); i++) {
            components.get(i).update(dTime);
        }
    }
}
