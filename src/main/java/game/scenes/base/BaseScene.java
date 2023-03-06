package game.scenes.base;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ActionComponent;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.graphics.scene.Scene;
import game.action.MuteAction;
import game.entities.ui.SoundEntity;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to inherit from to create all menu scenes in an equal layout (background, generic buttons for sound, options etc., ...)
 */
public class BaseScene extends Scene {
    public BaseScene(String name, int id) {
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

        GenericButton exitButton = null;
        try {
            exitButton = new GenericButton(
                    "Exit",
                    IdGenerator.generateId(),
                    1800, 15,
                    64, 64, new ExitAction(),
                    ImageIO.read(new File("res/menus/gui/buttons/exit_button.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addEntityToScene(exitButton);

        GenericButton options = null;
        try {
            options = new GenericButton(
                    "options", IdGenerator.generateId(),
                    1700, 15, 64, 64,
                    new StartAction(-249),
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
                    1600, 15, 64, 64,
                    new StartAction(null),
                    ImageIO.read(new File("res/menus/gui/speaker.png"))
            );
            sound.getComponent(ActionComponent.class).addAction(MouseEvent.BUTTON1, new MuteAction());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(sound);

        GenericButton mainMenuButton = null;
        try {
            mainMenuButton = new GenericButton(
                    "Menu_button",
                    IdGenerator.generateId(),
                    1500, 15,
                    64, 64, new StartAction(-255),
                    ImageIO.read(new File("res/menus/gui/buttons/menu_icon.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addEntityToScene(mainMenuButton);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        for (Entity e : getEntities()) {
            e.update();
        }
    }
}
