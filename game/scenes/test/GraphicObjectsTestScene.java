package game.scenes.test;

import com.Game;
import com.IdGenerator;
import com.ecs.entity.NumberSelectorEntity;
import com.graphics.scene.Scene;

import java.awt.*;

public class GraphicObjectsTestScene extends Scene {
    public GraphicObjectsTestScene(String name, int id) {
        super(name, id);

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);

        NumberSelectorEntity numberSelectorEntity = new NumberSelectorEntity("Number Selector", IdGenerator.generateId(),
                500,500,300,100, font,
                new Color(200, 0, 0, 255), new Color(0, 200, 0, 255), new Color(0, 0, 200, 255));

        addEntityToScene(numberSelectorEntity);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }
}
