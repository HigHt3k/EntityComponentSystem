package game.scenes.menu;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ActionComponent;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.graphics.scene.Scene;
import engine.resource.colorpalettes.Bit8;
import engine.resource.fonts.FontCollection;
import game.action.ChangeNameAction;
import game.action.MuteAction;
import game.entities.ui.SoundEntity;
import game.entities.ui.TextBody;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MenuScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 350;
    private static final int ITEM_HEIGHT = 60;

    public MenuScene(String name, int id) {
        super(name, id);

        try {
            SoundEntity sound = new SoundEntity("menu background sound", IdGenerator.generateId(), AudioSystem.getAudioInputStream(
                    new File("res/sound/2020-06-18_-_8_Bit_Retro_Funk_-_www.FesliyanStudios.com_David_Renda.wav")));
            addEntityToScene(sound);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Font font = FontCollection.scaleFont(FontCollection.bit8Font, 40f);

        // Create the Menu GUI
        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("res/cablesBackground.png")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        try {
            ImageEntity background = new ImageEntity("uni-stuttgart-logo", IdGenerator.generateId(),
                    ImageIO.read(new File("res/logos/unistuttgart_logo_deutsch_cmyk-01.png")), 25, 25, 766 / 3, 193 / 3, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        TextBody playerName = new TextBody("profile name", IdGenerator.generateId(),
                1300, 70, 300, 64, FontCollection.bit8FontLarge, Bit8.DARK_GREY, Game.config().getProfile().profile
        );
        addEntityToScene(playerName);
        //TODO: add button to change name
        GenericButton edit = null;
        try {
            edit = new GenericButton(
                    "edit", IdGenerator.generateId(),
                    1600, 50, 64, 64,
                    new ChangeNameAction(playerName),
                    ImageIO.read(new File("res/menus/gui/pen.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(edit);

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

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3", font, new ExitAction(),
                Bit8.DARK_GREY,null, null
        );
        addEntityToScene(exitButton);

        GenericButton options = null;
        try {
            options = new GenericButton(
                    "options", IdGenerator.generateId(),
                    1800, 50, 64, 64,
                    new StartAction(new OptionsScene("options", -249)),
                    ImageIO.read(new File("res/menus/gui/gear.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(options);

        GenericButton sound = null;
        try {
            sound = new GenericButton(
                    "sound", IdGenerator.generateId(),
                    1700, 50, 64, 64,
                    new StartAction(null),
                    ImageIO.read(new File("res/menus/gui/speaker.png"))
            );
            sound.getComponent(ActionComponent.class).addAction(MouseEvent.BUTTON1, new MuteAction());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(sound);
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
