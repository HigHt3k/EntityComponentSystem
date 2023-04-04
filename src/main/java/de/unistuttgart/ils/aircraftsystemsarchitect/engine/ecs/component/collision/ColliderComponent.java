package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.util.ArrayList;

public class ColliderComponent extends Component {
    private final ArrayList<CollisionObject> collisionObjects;

    public ColliderComponent() {
        this.collisionObjects = new ArrayList<>();
    }

    public ArrayList<CollisionObject> getCollisionObjects() {
        return collisionObjects;
    }

    public void addCollisionObject(CollisionObject collisionObject) {
        collisionObjects.add(collisionObject);
    }

    public void deactivateAllObjects() {
        for(CollisionObject c : collisionObjects) {
            c.setDeactivated(true);
        }
    }

    public void activateAllObjects() {
        for(CollisionObject c : collisionObjects) {
            c.setDeactivated(false);
        }
    }
}