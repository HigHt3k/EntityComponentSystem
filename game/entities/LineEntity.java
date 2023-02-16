package game.entities;

import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.LineObject;
import engine.ecs.entity.Entity;

import java.awt.*;

public class LineEntity extends Entity {
    public LineEntity(String name, int id, Point p1, Point p2, int thickness, Color color) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new LineObject(p1, null, Layer.GAMELAYER1, p1, p2, color, thickness));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
