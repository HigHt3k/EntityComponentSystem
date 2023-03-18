package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ActionComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.*;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render.TextHorizontalAlignment;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render.TextVerticalAlignment;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation.BuildPanelEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.SetAmountAction;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class NumberChooser extends Entity {
    private final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);

    public NumberChooser(String name, int id, String text, int x, int y, int width,
                         int height, int amount, BuildPanelEntity c) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

        ShapeObject shapeObject = new ShapeObject(new Point(x, y),
                new Rectangle(x, y, width, height), Layer.UI, Bit8.TRANSPARENT, Bit8.TRANSPARENT, 1);
        renderComponent.addRenderObject(shapeObject);

        renderComponent.addRenderObject(new TextObject(new Point(x, y),
                new Rectangle(x, y, width, height), Layer.UI, text, FontCollection.bit8Font, Bit8.DARK_GREY,
                TextHorizontalAlignment.CENTER, TextVerticalAlignment.CENTER));

        HoverObject hover = new HoverObject(new Point(x, y), new Rectangle(x, y, width, height), HOVER_COLOR, Layer.UI_HOVER);
        renderComponent.addRenderObject(hover);

        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);
        colliderComponent.addCollisionObject(new CollisionObject(new Rectangle(x, y, width, height), hover));

        ActionComponent actionComponent = new ActionComponent();
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
        actionComponent.addAction(MouseEvent.BUTTON1, new SetAmountAction(c, amount));
    }

    public NumberChooser(String name, int id, BufferedImage img, int x, int y, int width,
                         int height, int amount, BuildPanelEntity c) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

        ShapeObject shapeObject = new ShapeObject(new Point(x, y),
                new Rectangle(x, y, width, height), Layer.UI, Bit8.TRANSPARENT, Bit8.TRANSPARENT, 1);
        renderComponent.addRenderObject(shapeObject);

        renderComponent.addRenderObject(new ImageObject(new Point(x, y), new Rectangle(x, y, width, height), Layer.UI, img));

        HoverObject hover = new HoverObject(new Point(x, y), new Rectangle(x, y, width, height), HOVER_COLOR, Layer.UI_HOVER);
        renderComponent.addRenderObject(hover);

        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);
        colliderComponent.addCollisionObject(new CollisionObject(new Rectangle(x, y, width, height), hover));

        ActionComponent actionComponent = new ActionComponent();
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
        actionComponent.addAction(MouseEvent.BUTTON1, new SetAmountAction(c, amount));
    }
}

