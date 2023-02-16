package engine.ecs.component.collision;

import engine.Game;
import engine.ecs.component.graphics.GraphicsObject;

import java.awt.*;

@Deprecated
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
