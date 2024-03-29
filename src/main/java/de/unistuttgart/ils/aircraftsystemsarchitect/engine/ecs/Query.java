package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Query class implements static methods to query the currently active scene for
 * different entities by using either ids, names of component classes that are attached to an entity.
 */
public class Query {

    /**
     * Query all available entities in the current scene for entities with a specified component.
     * @param componentClass: The component to query for
     * @param <T>
     * @return: A list of all entities with the specified component
     */
    public static synchronized <T extends Component> ArrayList<Entity> getEntitiesWithComponent(Class<T> componentClass) {
        ArrayList<Entity> entities = new ArrayList<>();
        if (Game.scene().current() == null) {
            return entities;
        }
        for (Entity e : (ArrayList<Entity>) Game.scene().current().getEntities().clone()) {
            if (e.getComponent(componentClass) != null) {
                entities.add(e);
            }
        }
        return entities;
    }

    /**
     * Query a list for entities with a component class attached to them.
     * @param componentClass: The component to query for
     * @param <T>
     * @param filter: prefiltered list
     * @return: A list of all entities with the specified component
     */
    public static <T extends Component> ArrayList<Entity> getEntitiesWithComponent(Class<T> componentClass, ArrayList<Entity> filter) {
        ArrayList<Entity> entities = new ArrayList<>();
        if (Game.scene().current() == null) {
            return entities;
        }
        for (Entity e : filter) {
            if (e.getComponent(componentClass) != null) {
                entities.add(e);
            }
        }
        return entities;
    }

    /**
     * Query all available entities in the current scene for entities as an instance of the input class
     *
     * @param entityClass: class of the entities to query for
     * @param <T>
     * @return List with entities that were queried
     */
    public static <T extends Entity> ArrayList<T> getEntitiesOfClassType(Class<T> entityClass) {
        ArrayList<T> entities = new ArrayList<>();
        for (Entity e : Game.scene().current().getEntities()) {
            if (e.getClass() == entityClass) {
                entities.add(entityClass.cast(e));
            }
        }
        return entities;
    }

    /**
     * Query all available entities in the current scene for entities with a list of components
     *
     * @param componentClasses: classes of the components attached to the entities to query for
     * @return List with entities that were queried by the system
     * @param <T>
     */
    public static <T extends Entity> ArrayList<Entity> getEntitiesWithComponents(Class<T>[] componentClasses) {
        ArrayList<Entity> entities = new ArrayList<>();
        for(Entity e : Game.scene().current().getEntities()) {
            boolean hasAllComponents = true;
            for(Class<T> c : componentClasses) {
                if(e.getClass() != c) {
                    hasAllComponents = false;
                    break;
                }
            }
            if(hasAllComponents) {
                entities.add(e);
            }
        }
        return entities;
    }

    /**
     * get an entity from the current scene by its name
     * @param name: the entity name
     * @return: the entity if found, or null if not
     */
    public static Entity getEntityWithName(String name) {
        for(Entity e : Game.scene().current().getEntities()) {
            if(Objects.equals(e.getName(), name)) {
                return e;
            }
        }
        return null;
    }
}
