package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.HoverObject;

import java.awt.*;

/**
 * CollisionObjects can be attached to a ColliderComponent. They describe areas of the screen (by using bounds)
 * that should be treated as a collider by any system using the collider component
 */
public class CollisionObject {
    private Shape collisionBoundaries;
    private boolean isClicked = false;
    private boolean isHovered = false;
    private final HoverObject connectedHoverObject;
    private boolean deactivated = false;

    /**
     * Create a new CollisionObject
     * @param collisionBoundaries: boundaries of the collision box
     * @param connectedHoverObject: enables hovering of a graphical object, if null, hovering render is disabled
     */
    public CollisionObject(Shape collisionBoundaries, HoverObject connectedHoverObject) {
        this.collisionBoundaries = collisionBoundaries;
        this.connectedHoverObject = connectedHoverObject;
    }

    /**
     * Check if the object is disabled
     * @return true if disabled, false if not
     */
    public boolean isDeactivated() {
        return deactivated;
    }

    /**
     * Set the activation status of this object
     * @param deactivated: true if deactivated, false if not
     */
    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
    }

    /**
     * get the boundaries of the collider
     * @return the boundaries
     */
    public Shape getCollisionBoundaries() {
        return collisionBoundaries;
    }

    /**
     * Check if the collider was clicked before
     * @return true if clicked
     */
    public boolean isClicked() {
        return isClicked;
    }

    /**
     * Set the click state
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    /**
     * Check if object is hovered
     * @return true if hovered
     */
    public boolean isHovered() {
        return isHovered;
    }

    /**
     * Set hover state of object
     * @param hovered
     */
    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    /**
     * get the connected object that should be hovered
     * @return the object
     */
    public HoverObject getConnectedHoverObject() {
        return connectedHoverObject;
    }

    /**
     * Set the collision boundaries
     * @param collisionBoundaries: collider boundaries
     */
    public void setCollisionBoundaries(Shape collisionBoundaries) {
        this.collisionBoundaries = collisionBoundaries;
    }
}
