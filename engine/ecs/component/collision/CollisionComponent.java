package engine.ecs.component.collision;

import engine.Game;
import engine.ecs.component.Component;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CollisionComponent extends Component {
    Shape _COLLISIONBOX;
    Shape collisionBox;

    ArrayList<ColliderObject> colliders = new ArrayList<>();

    @Override
    public void update() {

    }

    /**
     * Detects a collision with the current collision box of this component
     * @param p: point to determine collision state for
     * @return false if no collision, true if collision
     */
    public boolean contains(Point2D p) {
        if (collisionBox.contains(p)) {
            return true;
        }
        return false;
    }

    public void setCollisionBox(Shape collisionBox) {
        this._COLLISIONBOX = collisionBox;
        updateSize();
    }

    public Shape getCollisionBox() {
        return collisionBox;
    }

    public void updateSize() {
        if(this._COLLISIONBOX != null)
            this.collisionBox = Game.scale().scaleShape(_COLLISIONBOX);
    }

    public void addCollider(ColliderObject collider) {
        colliders.add(collider);
    }

    public ArrayList<ColliderObject> getColliders() {
        return colliders;
    }
}
