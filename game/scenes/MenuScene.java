package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.Entity;
import com.ecs.entity.GenericButton;
import com.ecs.intent.ExitIntent;
import com.graphics.scene.Scene;
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

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);

        // Create the Menu GUI
        Entity background = new Entity("Background", IdGenerator.generateId());
        GraphicsComponent backgroundGraphicsComponent = new GraphicsComponent();
        backgroundGraphicsComponent.setBounds(new Rectangle(
                        0,
                        0,
                        1920,
                        1080
                )
        );

        try {
            backgroundGraphicsComponent.setImage(ImageIO.read(new File("game/res/bottom-view-plane-sky.jpg")));
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        background.addComponent(backgroundGraphicsComponent);
        backgroundGraphicsComponent.setEntity(background);
        addEntityToScene(background);

        GenericButton playButton = new GenericButton(
                "Play", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 0,
                ITEM_WIDTH, ITEM_HEIGHT,
                "PLAY", font
        );
        playButton.addIntent(new StartIntent(new LevelScene("Level", -254)));
        addEntityToScene(playButton);

        GenericButton buildButton = new GenericButton(
                "Build", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 1,
                ITEM_WIDTH, ITEM_HEIGHT,
                "BUILD", font
        );
        buildButton.addIntent(new StartIntent(new BuildScene("build", -250)));
        addEntityToScene(buildButton);

        GenericButton optionsButton = new GenericButton(
                "Options", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "OPTIONS", font
        );
        addEntityToScene(optionsButton);

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH/2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 3,
                ITEM_WIDTH, ITEM_HEIGHT,
                "EXIT", font
        );
        exitButton.addIntent(new ExitIntent());
        addEntityToScene(exitButton);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }
}
