package engine.ecs.component.collision;

import java.awt.*;

public class CollisionObject {
    private Shape collisionBoundaries;
    private boolean isClicked = false;
    private boolean isHovered = false;

    public CollisionObject(Shape collisionBoundaries) {
        this.collisionBoundaries = collisionBoundaries;
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
}
