package com.ecs.component.object;

import java.awt.*;

public class ColliderObject {
    Shape collisionBox;
    GraphicsObject correspondingGraphics;

    public ColliderObject(Shape collisionBox) {
        this.collisionBox = collisionBox;
    }

    public ColliderObject(Shape collisionBox, GraphicsObject correspondingGraphics) {
        this.collisionBox = collisionBox;
        this.correspondingGraphics = correspondingGraphics;
    }

    public Shape getCollisionBox() {
        return collisionBox;
    }

    public GraphicsObject getCorrespondingGraphics() {
        return correspondingGraphics;
    }
}
