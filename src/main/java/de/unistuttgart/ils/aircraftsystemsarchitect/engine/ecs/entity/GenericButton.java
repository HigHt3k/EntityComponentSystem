package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ActionComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.*;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.CollisionObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * GenericButton:
 *         |_ GraphicsComponent
 *         |_ CollisionComponent
 *         |_ IntentComponent
 *               |_ HoverIntent
 *         |_ addIntent();
 */
public class GenericButton extends Entity {
    private Color TEXT_COLOR = Bit8.DARK_GREY;
    private Color BOX_COLOR = Bit8.CHROME;
    private Color BOX_BORDER_COLOR = Bit8.GREY;
    private final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);
    protected TextObject text;
    protected ShapeObject button;
    protected HoverObject hover;

    public GenericButton(String name, int id, int x, int y, int width, int height, String text, Font font, Action action, Color textColor, Color boxColor, Color fillColor) {
        super(name, id);
        this.BOX_COLOR = fillColor;
        this.TEXT_COLOR = textColor;
        this.BOX_BORDER_COLOR = boxColor;

        // Define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics Component properties

        RenderComponent renderComponent = new RenderComponent();
        this.button = new ShapeObject(new Point(x, y), bounds, Layer.UI, BOX_COLOR, BOX_BORDER_COLOR, 1);
        this.text = new TextObject(new Point(x, y), bounds, Layer.UI, text, font, TEXT_COLOR);
        this.hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR);
        renderComponent.addRenderObject(this.button);
        renderComponent.addRenderObject(this.text);
        renderComponent.addRenderObject(this.hover);
        this.addComponent(renderComponent);
        renderComponent.setEntity(this);

        // Collider
        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.addCollisionObject(new CollisionObject(bounds, this.hover));
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);

        ActionComponent actionComponent = new ActionComponent();
        actionComponent.addAction(MouseEvent.BUTTON1, action);
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
    }

    public GenericButton(String name, int id, int x, int y, int width, int height, Action action, BufferedImage img) {
        super(name, id);

        // Define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics Component properties

        RenderComponent renderComponent = new RenderComponent();
        this.hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR, Layer.UI_HOVER);
        renderComponent.addRenderObject(this.hover);
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), bounds, Layer.UI, img));
        this.addComponent(renderComponent);
        renderComponent.setEntity(this);

        // Collider
        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.addCollisionObject(new CollisionObject(bounds, this.hover));
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);

        ActionComponent actionComponent = new ActionComponent();
        actionComponent.addAction(MouseEvent.BUTTON1, action);
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
    }

    /**
     * Create a generic functional button. addIntent needs to be used to add a purpose other than hovering to this button.
     * @param name: Name of the entity
     * @param id: Id of the entity
     * @param x: x position by design
     * @param y: y position by design
     * @param width: design width
     * @param height: design height
     * @param text: text to display (Centered) at the button
     * @param font: Font to use for this button
     */
    public GenericButton(String name, int id, int x, int y, int width, int height, String text, Font font, Action action) {
        super(name, id);

        // Define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics Component properties

        RenderComponent renderComponent = new RenderComponent();
        this.button = new ShapeObject(new Point(x, y), bounds, Layer.UI, BOX_COLOR, BOX_BORDER_COLOR, 1);
        this.text = new TextObject(new Point(x, y), bounds, Layer.UI, text, font, TEXT_COLOR);
        this.hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR, Layer.UI_HOVER);
        renderComponent.addRenderObject(this.button);
        renderComponent.addRenderObject(this.text);
        renderComponent.addRenderObject(this.hover);
        this.addComponent(renderComponent);
        renderComponent.setEntity(this);

        // Collider
        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.addCollisionObject(new CollisionObject(bounds, this.hover));
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);

        ActionComponent actionComponent = new ActionComponent();
        actionComponent.addAction(MouseEvent.BUTTON1, action);
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
    }
}
