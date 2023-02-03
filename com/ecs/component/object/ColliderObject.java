package com.ecs.component.object;

import java.awt.*;

public class ColliderObject {
    Shape collisionBox;

    public ColliderObject(Shape collisionBox) {
        this.collisionBox = collisionBox;
    }

    public Shape getCollisionBox() {
        return collisionBox;
    }
}
