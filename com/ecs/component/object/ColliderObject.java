package com.ecs.component.object;

import com.Game;

import java.awt.*;

public class ColliderObject {
    Shape collisionBox;
    GraphicsObject correspondingGraphics;

    public ColliderObject(Shape collisionBox) {
        this.collisionBox = Game.scale().scaleShape(collisionBox);
    }

    public ColliderObject(Shape collisionBox, GraphicsObject correspondingGraphics) {
        this.collisionBox = Game.scale().scaleShape(collisionBox);
        this.correspondingGraphics = correspondingGraphics;
    }

    public Shape getCollisionBox() {
        return collisionBox;
    }

    public GraphicsObject getCorrespondingGraphics() {
        return correspondingGraphics;
    }
}
