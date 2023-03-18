package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.menu;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.StartAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.GenericButton;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.ImageEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.SoundEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base.BaseMenuScene;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenuScene extends BaseMenuScene {

    public MainMenuScene(String name, int id) {
        super(name, id);

        try {
            SoundEntity sound = new SoundEntity("menu background sound", IdGenerator.generateId(), AudioSystem.getAudioInputStream(
                    new File("res/sound/2020-06-18_-_8_Bit_Retro_Funk_-_www.FesliyanStudios.com_David_Renda.wav")));
            addEntityToScene(sound);
        } catch (UnsupportedAudioFileException e) {
            Game.logger().severe("Unsupported Audio File!" + e.getMessage());
        } catch (IOException e) {
            Game.logger().severe("Input Error!" + e.getMessage());
        }

        try {
            ImageEntity logo = new ImageEntity("skylogic-logo", IdGenerator.generateId(),
                    ImageIO.read(new File("res/logos/skylogic-logo.png")), (int) (1920/2 - (515*1.5)/2), 100, (int) (515 * 1.5), 300, Layer.UI);
            addEntityToScene(logo);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        Font font = FontCollection.scaleFont(FontCollection.bit8Font, 40f);
        int ITEM_MARGIN = 20;
        int ITEM_HEIGHT = 60;
        int ITEM_WIDTH = 200;
        GenericButton playButton = new GenericButton(
                "Play", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 500 + 0,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@0", font, new StartAction(-254),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(playButton);

        GenericButton buildButton = new GenericButton(
                "Build", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 500 + (ITEM_HEIGHT + ITEM_MARGIN),
                ITEM_WIDTH, ITEM_HEIGHT,
                "@1", font, new StartAction(-250),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(buildButton);
    }
}
