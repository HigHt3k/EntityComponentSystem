package game.scenes.menu;

import engine.IdGenerator;
import engine.ecs.component.action.ChangeLanguageAction;
import engine.ecs.entity.GenericButton;
import engine.resource.colorpalettes.Bit8;
import engine.resource.fonts.FontCollection;
import engine.resource.lang.LanguageType;
import game.scenes.base.BaseMenuScene;

import java.awt.*;

public class OptionsMenuScene extends BaseMenuScene {

    public OptionsMenuScene(String name, int id) {
        super(name, id);

        Font font = FontCollection.scaleFont(FontCollection.bit8Font, 25f);

        int ITEM_MARGIN = 20;
        int ITEM_WIDTH = 350;
        int ITEM_HEIGHT = 60;
        GenericButton toggleLanguageEnglish = new GenericButton(
                "English", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 0,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@10", font, new ChangeLanguageAction(LanguageType.EN_US),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(toggleLanguageEnglish);

        GenericButton toggleLanguageGerman = new GenericButton(
                "German", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 1,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@11", font, new ChangeLanguageAction(LanguageType.DE_DE),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(toggleLanguageGerman);

        GenericButton toggleLanguageGermanEasy = new GenericButton(
                "GermanSimple", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@12", font, new ChangeLanguageAction(LanguageType.DE_SIMPLE),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(toggleLanguageGermanEasy);

        //TODO: implement controller / mouse sensitivity
    }
}
