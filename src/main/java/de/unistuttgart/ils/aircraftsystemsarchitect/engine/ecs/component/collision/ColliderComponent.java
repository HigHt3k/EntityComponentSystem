package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.util.ArrayList;

/**
 * ColliderComponent is used to store collision objects of an entity, that should be considered
 * when checking different objects or inputs for collisions.
 */
public class ColliderComponent extends Component {
    private final ArrayList<CollisionObject> collisionObjects;

    /**
     * Creates a new ColliderComponent
     */
    public ColliderComponent() {
        this.collisionObjects = new ArrayList<>();
    }

    /**
     * Get a list of all collision objects stored in this component
     * @return a list of CollisionObjects
     */
    public ArrayList<CollisionObject> getCollisionObjects() {
        return collisionObjects;
    }

    /**
     * add a collision object to this component
     * @param collisionObject: the collision object to add
     */
    public void addCollisionObject(CollisionObject collisionObject) {
        collisionObjects.add(collisionObject);
    }

    /**
     * disables the usage of all objects stored in this component for action handling
     */
    public void deactivateAllObjects() {
        for(CollisionObject c : collisionObjects) {
            c.setDeactivated(true);
        }
    }

    /**
     * enables the usage of all objects stored in this component for action handling
     */
    public void activateAllObjects() {
        for(CollisionObject c : collisionObjects) {
            c.setDeactivated(false);
        }
    }
}
