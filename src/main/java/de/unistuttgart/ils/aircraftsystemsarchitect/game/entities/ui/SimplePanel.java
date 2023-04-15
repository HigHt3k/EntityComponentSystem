package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ShapeObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

/**
 * A simple UI panel with a background and border color.
 */
public class SimplePanel extends Entity {

    /**
     * Creates a new SimplePanel object with a given name, ID, position, size, background and border color.
     * @param name the name of the panel
     * @param id the ID of the panel
     * @param x the x position of the panel
     * @param y the y position of the panel
     * @param width the width of the panel
     * @param height the height of the panel
     * @param background the background color of the panel
     * @param border the border color of the panel
     * @param textColor the text color of the panel
     */
    public SimplePanel(String name, int id,
                       int x, int y, int width, int height,
                       Color background, Color border, Color textColor) {
        super(name, id);

        Rectangle r = new Rectangle(x, y, width, height); // Position

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ShapeObject(new Point(x, y), r, Layer.UI, background, border, 5));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }

    /**
     * Creates a new SimplePanel object with a given name, ID, position, size, background and border color on a given layer.
     * @param name the name of the panel
     * @param id the ID of the panel
     * @param x the x position of the panel
     * @param y the y position of the panel
     * @param width the width of the panel
     * @param height the height of the panel
     * @param background the background color of the panel
     * @param border the border color of the panel
     * @param textColor the text color of the panel
     * @param layer the layer on which the panel should be rendered
     */
    public SimplePanel(String name, int id,
                       int x, int y, int width, int height,
                       Color background, Color border, Color textColor,
                       Layer layer) {
        super(name, id);

        Rectangle r = new Rectangle(x, y, width, height); // Position

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ShapeObject(new Point(x, y), r, layer, background, border, 5));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
