package com.ecs.entity;

import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.object.ColliderObject;
import com.ecs.component.object.GraphicsObject;
import org.w3c.dom.css.Rect;

import java.awt.*;

public class NumberSelectorEntity extends Entity {
    private ColliderObject left;
    private ColliderObject right;


    private Entity change;

    /**
     * Create a new number selector
     * @param name
     * @param id
     */
    public NumberSelectorEntity(String name, int id,
                                int x, int y, int width, int height,
                                Font font, Color textColor,
                                Color fillColor, Color borderColor) {
        super(name, id);

        Rectangle leftShape = new Rectangle(x, y, width / 3, height);
        Rectangle middleShape = new Rectangle(x + width/3, y, width/3, height);
        Rectangle rightShape = new Rectangle(x + width/3*2, y, width/3, height);

        GraphicsComponent graphics = new GraphicsComponent();

        GraphicsObject minusButtonText = new GraphicsObject("-", font, textColor, leftShape);
        GraphicsObject plusButtonText = new GraphicsObject("+", font, textColor, rightShape);
        minusButtonText.setLayer(1);
        plusButtonText.setLayer(1);

        GraphicsObject minusButtonBox = new GraphicsObject(leftShape, fillColor, borderColor);
        GraphicsObject displayFieldBox = new GraphicsObject(middleShape, null, borderColor);
        GraphicsObject plusButtonBox = new GraphicsObject(rightShape, fillColor, borderColor);

        graphics.addGraphicsObject(minusButtonText);
        graphics.addGraphicsObject(plusButtonText);
        graphics.addGraphicsObject(minusButtonBox);
        graphics.addGraphicsObject(displayFieldBox);
        graphics.addGraphicsObject(plusButtonBox);

        graphics.setEntity(this);
        this.addComponent(graphics);

        CollisionComponent colliders = new CollisionComponent();
        left = new ColliderObject(leftShape, minusButtonBox);
        right = new ColliderObject(rightShape, plusButtonBox);
        colliders.addCollider(left);
        colliders.addCollider(right);
        colliders.setEntity(this);
        this.addComponent(colliders);
    }

    public ColliderObject getLeft() {
        return left;
    }

    public ColliderObject getRight() {
        return right;
    }

    public Entity getChange() {
        return change;
    }

    public void setChange(Entity change) {
        this.change = change;
    }
}
