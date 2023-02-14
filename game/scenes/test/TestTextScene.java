package game.scenes.test;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.entity.Entity;
import engine.graphics.scene.Scene;

import java.awt.*;

public class TestTextScene extends Scene {
    public TestTextScene(String name, int id) {
        super(name, id);

        Entity e = new Entity("Test", IdGenerator.generateId());

        GraphicsComponent graphics = new GraphicsComponent();

        Rectangle r = new Rectangle(400, 400, 400, 400);
        graphics.setBounds(r);
        graphics.addShape(r);
        graphics.setBorderColor(new Color(255, 0, 0, 255));
        graphics.addLocation(r.getLocation());
        graphics.addText("This is a test regarding automatic line breaks using regular expressions. There should not be any weird linebreaks in this box. Please also check if the margin to the borders looks good.");
        graphics.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        graphics.setTextColor(new Color(30, 30, 80, 255));

        graphics.setEntity(e);
        e.addComponent(graphics);

        addEntityToScene(e);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }
}
