package game.scenes.menu;

import engine.IdGenerator;
import engine.ecs.component.action.StartAction;
import engine.ecs.entity.GenericButton;
import engine.resource.colorpalettes.Bit8;
import engine.resource.fonts.FontCollection;
import game.scenes.base.BaseMenuScene;

import java.awt.*;

public class MainMenuScene extends BaseMenuScene {

    public MainMenuScene(String name, int id) {
        super(name, id);

        Font font = FontCollection.scaleFont(FontCollection.bit8Font, 40f);
        int ITEM_MARGIN = 20;
        int ITEM_HEIGHT = 60;
        int ITEM_WIDTH = 350;
        GenericButton playButton = new GenericButton(
                "Play", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 0,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@0", font, new StartAction(-254),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(playButton);

        GenericButton buildButton = new GenericButton(
                "Build", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 1,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@1", font, new StartAction(-250),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(buildButton);
    }
}
