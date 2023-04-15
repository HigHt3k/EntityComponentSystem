package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

/**
 * An entity representing a block of text with a specific font and color.
 */
public class TextBody extends Entity {

    /**
     * Constructs a new {@code TextBody} entity with the given parameters.
     *
     * @param name the name of the entity
     * @param id the ID of the entity
     * @param x the X-coordinate of the entity's position
     * @param y the Y-coordinate of the entity's position
     * @param width the width of the entity
     * @param height the height of the entity
     * @param font the font of the text
     * @param textColor the color of the text
     * @param text the text to be displayed
     */
    public TextBody(String name, int id, int x, int y, int width, int height,
                    Font font, Color textColor, String text) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new TextObject(new Point(x, y), new Rectangle(x, y, width, height),
                Layer.UI, text, font, textColor));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
