package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ShapeObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

public class SimplePanel extends Entity {

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
