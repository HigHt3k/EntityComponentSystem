package engine.ecs.component.collision;

import engine.ecs.component.graphics.objects.HoverObject;

import java.awt.*;

public class CollisionObject {
    private Shape collisionBoundaries;
    private boolean isClicked = false;
    private boolean isHovered = false;
    private final HoverObject connectedHoverObject;

    public CollisionObject(Shape collisionBoundaries, HoverObject connectedHoverObject) {
        this.collisionBoundaries = collisionBoundaries;
        this.connectedHoverObject = connectedHoverObject;
    }

    public Shape getCollisionBoundaries() {
        return collisionBoundaries;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public HoverObject getConnectedHoverObject() {
        return connectedHoverObject;
    }

    public void setCollisionBoundaries(Shape collisionBoundaries) {
        this.collisionBoundaries = collisionBoundaries;
    }
}
