package game.entities;

import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.entity.Entity;

import java.awt.*;

public class ScoreBox extends Entity {
    public ScoreBox(String name, int id, Font font, int score,
                    int x, int y, int width, int height, String text) {
        super(name, id);

        GraphicsComponent graphics = new GraphicsComponent();

        Rectangle r = new Rectangle(x, y, width, height);

        graphics.setShape(r);
        graphics.setBounds(r);
        graphics.setTextColor(new Color(230, 70, 20, 255));
        graphics.setFillColor(new Color(40, 40, 100, 255));
        graphics.setBorderColor(new Color(50,50,50,255));
        graphics.addText(text + score);
        graphics.addLocation(r.getLocation());
        graphics.setFont(font);
        graphics.setEntity(this);
        this.addComponent(graphics);
    }
}
