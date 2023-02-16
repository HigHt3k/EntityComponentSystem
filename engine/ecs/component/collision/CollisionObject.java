package engine.ecs.component.collision;

import java.awt.*;

public class CollisionObject {
    private Shape collisionBoundaries;

    public CollisionObject(Shape collisionBoundaries) {
        this.collisionBoundaries = collisionBoundaries;
    }

    public Shape getCollisionBoundaries() {
        return collisionBoundaries;
    }
}
