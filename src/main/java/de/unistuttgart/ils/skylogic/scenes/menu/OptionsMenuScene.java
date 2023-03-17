package de.unistuttgart.ils.skylogic.scenes.menu;

import de.unistuttgart.ils.skyengine.IdGenerator;
import de.unistuttgart.ils.skyengine.ecs.component.action.ChangeLanguageAction;
import de.unistuttgart.ils.skyengine.ecs.entity.GenericButton;
import de.unistuttgart.ils.skyengine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.skyengine.resource.fonts.FontCollection;
import de.unistuttgart.ils.skyengine.resource.lang.LanguageType;
import de.unistuttgart.ils.skylogic.action.ToggleFullScreenAction;
import de.unistuttgart.ils.skylogic.scenes.base.BaseMenuScene;

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

        GenericButton toggleFullScreen = new GenericButton(
                "fullscreen", IdGenerator.generateId(),
                1920/2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 3,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@13", font, new ToggleFullScreenAction(),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(toggleFullScreen);

        //TODO: implement controller / mouse sensitivity
    }
}
