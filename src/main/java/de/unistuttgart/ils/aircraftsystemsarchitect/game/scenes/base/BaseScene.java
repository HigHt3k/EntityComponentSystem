package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ActionComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ExitAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.StartAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.GenericButton;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene.Scene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.MuteAction;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to inherit from to create all menu scenes in an equal layout (background, generic buttons for sound, options etc., ...)
 */
public abstract class BaseScene extends Scene {
    public BaseScene(String name, int id) {
        super(name, id);

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
}
