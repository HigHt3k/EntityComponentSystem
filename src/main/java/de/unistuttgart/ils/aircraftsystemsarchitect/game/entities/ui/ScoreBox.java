package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ShapeObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;

import java.awt.*;

/**
 * A UI element that displays a score box with a given font and score value.
 */
public class ScoreBox extends Entity {

    /**
     * Constructs a new ScoreBox with the given name, ID, font, score value, position and size.
     *
     * @param name   the name of the entity
     * @param id     the ID of the entity
     * @param font   the font used to display the score value
     * @param score  the score value to display
     * @param x      the x-coordinate of the upper-left corner of the score box
     * @param y      the y-coordinate of the upper-left corner of the score box
     * @param width  the width of the score box
     * @param height the height of the score box
     * @param text   the text to display above the score box
     */
    public ScoreBox(String name, int id, Font font, int score,
                    int x, int y, int width, int height, String text) {
        super(name, id);

        Rectangle r = new Rectangle(x, y, width, height);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ShapeObject(new Point(x, y), r, Layer.UI, Bit8.PUMPKIN, Bit8.SCARLET, 1));
        renderComponent.addRenderObject(new TextObject(new Point(x, y), r, Layer.UI_FRONT, text, font, Bit8.DARK_GREY));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
