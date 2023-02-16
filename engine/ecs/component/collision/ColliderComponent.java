package engine.ecs.component.collision;

import engine.ecs.component.Component;

import java.util.ArrayList;

public class ColliderComponent extends Component {
    private final ArrayList<CollisionObject> collisionObjects;

    public ColliderComponent() {
        this.collisionObjects = new ArrayList<>();
    }

    @Override
    public void update() {

    }

    public ArrayList<CollisionObject> getCollisionObjects() {
        return collisionObjects;
    }
}
