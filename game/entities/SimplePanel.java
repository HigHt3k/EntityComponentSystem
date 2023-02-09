package game.entities;

import com.ecs.component.graphics.GraphicsComponent;
import com.ecs.entity.Entity;

import java.awt.*;

public class SimplePanel extends Entity {

    public SimplePanel(String name, int id,
                       int x, int y, int width, int height,
                       Color background, Color border, Color textColor) {
        super(name, id);

        Rectangle r = new Rectangle(x, y, width, height); // Position

        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(r);
        graphics.setShape(r);
        graphics.setFillColor(background);
        graphics.setBorderColor(border);
        graphics.setTextColor(textColor);
        graphics.setThickness(5);
        graphics.setEntity(this);
        this.addComponent(graphics);
    }
}
