package game.entities.ui;

import engine.ecs.component.action.ActionComponent;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.HoverObject;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.ShapeObject;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;
import engine.resource.colorpalettes.Bit8;
import engine.resource.fonts.FontCollection;
import game.action.GridSizeXAction;
import game.scenes.GridSize;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NumberChooserX extends Entity {
    private final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);

    public NumberChooserX(String name, int id, String text, int x, int y, int width,
                          int height, int amount, GridSize i) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

        ShapeObject shapeObject = new ShapeObject(new Point(x, y),
                new Rectangle(x, y, width, height), Layer.UI, Bit8.ORANGE, Bit8.SCARLET, 1);
        renderComponent.addRenderObject(shapeObject);

        renderComponent.addRenderObject(new TextObject(new Point(x, y),
                new Rectangle(x, y, width, height), Layer.UI, text, FontCollection.bit8Font, Bit8.DARK_GREY));

        HoverObject hover = new HoverObject(new Point(x, y), new Rectangle(x, y, width, height), HOVER_COLOR);
        renderComponent.addRenderObject(hover);

        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);
        colliderComponent.addCollisionObject(new CollisionObject(new Rectangle(x, y, width, height), hover));

        ActionComponent actionComponent = new ActionComponent();
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
        actionComponent.addAction(MouseEvent.BUTTON1, new GridSizeXAction(i, amount));
    }
}

