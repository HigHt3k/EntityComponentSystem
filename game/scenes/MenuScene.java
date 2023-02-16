package game.scenes;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.ecs.entity.SceneStartButton;
import engine.ecs.intent.ExitIntent;
import engine.ecs.system.CollisionDetectionSystem;
import engine.ecs.system.HoverSystem;
import engine.graphics.scene.Scene;
import game.handler.CursorSelectorHandler;
import game.intent.StartIntent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 350;
    private static final int ITEM_HEIGHT = 60;

    public MenuScene(String name, int id) {
        super(name, id);
        Game.input().removeAllHandlers();

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);

        // Create the Menu GUI
        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/bottom-view-plane-sky.jpg")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        SceneStartButton playButton = new SceneStartButton(
                "Play", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 0,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@0", font, new LevelScene("level", -254)
        );
        addEntityToScene(playButton);

        GenericButton buildButton = new GenericButton(
                "Build", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 1,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@1", font
        );
        buildButton.addIntent(new StartIntent(new BuildScene("build", -250)));
        addEntityToScene(buildButton);

        GenericButton optionsButton = new GenericButton(
                "Options", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@2", font
        );
        optionsButton.addIntent(new StartIntent(new OptionsScene("options",-249)));
        addEntityToScene(optionsButton);

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 3,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3", font
        );
        exitButton.addIntent(new ExitIntent());
        addEntityToScene(exitButton);
    }

    @Override
    public void init() {
        Game.system().resetSystems();
        Game.input().addHandler(new CursorSelectorHandler());
        Game.input().addHandler(new CollisionDetectionSystem());
        Game.system().addSystem(new HoverSystem());
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }
}
