package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.LineObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

public class LineEntity extends Entity {
    public LineEntity(String name, int id, Point p1, Point p2, int thickness, Color color) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new LineObject(p1, null, Layer.GAMELAYER1, p1, p2, color, thickness));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }

    public LineEntity(String name, int id, Point p1, Point p2, int thickness, Color color, Layer layer) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new LineObject(p1, null, layer, p1, p2, color, thickness));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}