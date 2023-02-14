package game.scenes.test;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.entity.NumberSelectorEntity;
import engine.graphics.scene.Scene;
import game.handler.CollisionHandler;

import java.awt.*;

public class GraphicObjectsTestScene extends Scene {
    public GraphicObjectsTestScene(String name, int id) {
        super(name, id);

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);

        NumberSelectorEntity numberSelectorEntity = new NumberSelectorEntity("Number Selector", IdGenerator.generateId(),
                500,500,300,100, font,
                new Color(200, 0, 0, 255), new Color(0, 200, 0, 255), new Color(0, 0, 200, 255));

        addEntityToScene(numberSelectorEntity);

        init();
    }

    @Override
    public void init() {
        Game.input().addHandler(new CollisionHandler());
    }

    @Override
    public void update() {

    }
}
