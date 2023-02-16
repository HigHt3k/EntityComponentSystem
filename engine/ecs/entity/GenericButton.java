package engine.ecs.entity;

import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.RenderObject;
import engine.ecs.component.graphics.objects.ShapeObject;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.intent.HoverIntent;
import engine.ecs.intent.Intent;
import engine.resource.colorpalettes.Bit8;
import org.w3c.dom.Text;

import java.awt.*;

/**
 * GenericButton:
 *         |_ GraphicsComponent
 *         |_ CollisionComponent
 *         |_ IntentComponent
 *               |_ HoverIntent
 *         |_ addIntent();
 */
public class GenericButton extends Entity {
    private static final Color TEXT_COLOR = Bit8.DARK_GREY;
    private static final Color BOX_COLOR = Bit8.CHROME;
    private static final Color BOX_BORDER_COLOR = Bit8.GREY;
    private static final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);
    protected TextObject text;
    protected ShapeObject button;

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
    public GenericButton(String name, int id, int x, int y, int width, int height, String text, Font font) {
        super(name, id);

        // Define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics Component properties

        RenderComponent renderComponent = new RenderComponent();
        this.button = new ShapeObject(new Point(x, y), bounds, Layer.UI, BOX_COLOR, BOX_BORDER_COLOR, 1);
        this.text = new TextObject(new Point(x, y), bounds, Layer.UI, text, font, TEXT_COLOR);
        renderComponent.addRenderObject(this.button);
        renderComponent.addRenderObject(this.text);
        this.addComponent(renderComponent);
        renderComponent.setEntity(this);

        // Collider
        CollisionComponent collider = new CollisionComponent();
        collider.setEntity(this);
        collider.setCollisionBox(bounds);
        this.addComponent(collider);

        // Intent Handler
        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        // Add Hovering
        HoverIntent hover = new HoverIntent();
        hover.setIntentComponent(intents);
        intents.addIntent(hover);
        this.addComponent(intents);
    }

    public void addIntent(Intent intent) {
        IntentComponent intents = this.getComponent(IntentComponent.class);
        intent.setIntentComponent(intents);
        intents.addIntent(intent);
    }
}
