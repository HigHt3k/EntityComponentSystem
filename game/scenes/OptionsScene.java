package game.scenes;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.ecs.intent.ChangeLanguageIntent;
import engine.ecs.intent.ExitIntent;
import engine.graphics.scene.Scene;
import engine.resource.lang.LanguageType;
import game.handler.CursorSelectorHandler;
import game.intent.StartIntent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OptionsScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 350;
    private static final int ITEM_HEIGHT = 60;
    private static final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private static final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private static final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    public OptionsScene(String name, int id) {
        super(name, id);

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);

        // Create the Menu GUI
        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/bottom-view-plane-sky.jpg")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 800,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font, new StartAction(Game.scene().getScene(-255))
        );

        mainMenuButton.addIntent(new StartIntent());
        this.addEntityToScene(mainMenuButton);

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3", font, new ExitAction()
        );
        exitButton.addIntent(new ExitIntent());
        addEntityToScene(exitButton);

        GenericButton toggleLanguageEnglish = new GenericButton(
                "English", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 0,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@10", font, new StartAction(null)
        );
        toggleLanguageEnglish.addIntent(new ChangeLanguageIntent(LanguageType.EN_US));
        addEntityToScene(toggleLanguageEnglish);

        GenericButton toggleLanguageGerman = new GenericButton(
                "German", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 1,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@11", font, new StartAction(null)
        );
        toggleLanguageGerman.addIntent(new ChangeLanguageIntent(LanguageType.DE_DE));
        addEntityToScene(toggleLanguageGerman);

        GenericButton toggleLanguageGermanEasy = new GenericButton(
                "GermanSimple", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@12", font, new StartAction(null)
        );
        toggleLanguageGermanEasy.addIntent(new ChangeLanguageIntent(LanguageType.DE_SIMPLE));
        addEntityToScene(toggleLanguageGermanEasy);
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
