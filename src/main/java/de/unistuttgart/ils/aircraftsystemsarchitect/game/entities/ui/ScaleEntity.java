package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.LineObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;

import java.awt.*;

/**
 * Represents a scale entity that contains markers and a line object to indicate a scale.
 */
public class ScaleEntity extends Entity {
    /**
     * Constructs a new ScaleEntity with the specified parameters.
     *
     * @param name    the name of the entity
     * @param id      the ID of the entity
     * @param x       the x-coordinate of the line's start point
     * @param y       the y-coordinate of the line's start point
     * @param width   the width of the line
     * @param height  the height of the line
     * @param marker1 the first marker of the scale
     * @param marker2 the second marker of the scale
     * @param marker3 the third marker of the scale
     */
    public ScaleEntity(String name, int id,
                       int x, int y, int width, int height,
                       Marker marker1, Marker marker2, Marker marker3) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
        renderComponent.addRenderObject(
                new LineObject(
                        new Point(x, y),
                        new Rectangle(x, y, width, height),
                        Layer.UI_FRONT,
                        new Point(x, y),
                        new Point(x + width, y),
                        Bit8.WHITE, 4
                )
        );
        renderComponent.addRenderObject(marker1.lineObject);
        renderComponent.addRenderObject(marker2.lineObject);
        renderComponent.addRenderObject(marker3.lineObject);
    }
}
