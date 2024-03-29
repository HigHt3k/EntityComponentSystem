package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.util.ArrayList;

/**
 * Entities are generic objects handled by the game engine.
 * They contain a list of unique {@link Component}s which describe an Entity, which are used by the
 * Game Engine Systems and Handlers to process data and change behavior.
 */
public class Entity implements Cloneable {

    private final ArrayList<Component> components;
    private String name;
    private final int id;
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

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
