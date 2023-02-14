package game.entities;

import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.entity.Entity;

import java.awt.*;

public class TextBody extends Entity {
    public TextBody(String name, int id, int x, int y, int width, int height,
                    Font font, Color textColor, String text) {
        super(name, id);

        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(new Rectangle(x, y, width, height));
        graphics.setFont(font);
        graphics.setTextColor(textColor);
        graphics.addText(text);
        graphics.addLocation(new Point(x, y));
        graphics.setEntity(this);
        this.addComponent(graphics);
    }
}
