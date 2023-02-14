package game.entities;

import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.entity.Entity;

import java.awt.*;

public class LineEntity extends Entity {
    public LineEntity(String name, int id, Point p1, Point p2, int thickness, Color color) {
        super(name, id);

        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setLine(p1, p2);
        graphics.setLineColor(color);
        graphics.setThickness(thickness);
        graphics.setEntity(this);
        this.addComponent(graphics);
    }
}
