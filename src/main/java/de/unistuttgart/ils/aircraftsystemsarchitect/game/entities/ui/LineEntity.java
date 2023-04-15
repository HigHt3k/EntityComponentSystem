package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.LineObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

/**
 * An entity that represents a line to be rendered in the game.
 */
public class LineEntity extends Entity {

    /**
     * Constructs a new line entity with the specified name, ID, start and end points, thickness, and color.
     * The line is rendered on the default layer, {@code Layer.GAMELAYER1}.
     *
     * @param name      the name of the entity
     * @param id        the ID of the entity
     * @param p1        the starting point of the line
     * @param p2        the ending point of the line
     * @param thickness the thickness of the line
     * @param color     the color of the line
     */
    public LineEntity(String name, int id, Point p1, Point p2, int thickness, Color color) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new LineObject(p1, null, Layer.GAMELAYER1, p1, p2, color, thickness));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }

    /**
     * Constructs a new line entity with the specified name, ID, start and end points, thickness, color,
     * and layer on which to render the line.
     *
     * @param name      the name of the entity
     * @param id        the ID of the entity
     * @param p1        the starting point of the line
     * @param p2        the ending point of the line
     * @param thickness the thickness of the line
     * @param color     the color of the line
     * @param layer     the layer on which to render the line
     */
    public LineEntity(String name, int id, Point p1, Point p2, int thickness, Color color, Layer layer) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new LineObject(p1, null, layer, p1, p2, color, thickness));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
