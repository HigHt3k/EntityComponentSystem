package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.GenericButton;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.ImageEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.TextBody;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.ChangeNameAction;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * This class can be used to create generic menu scenes with the same layout. Generified.
 */
public class BaseMenuScene extends BaseScene {

    public BaseMenuScene(String name, int id) {
        super(name, id);

        // Create the Menu GUI
        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("res/backgrounds/sky-not-animated.png")), 0, 0, 1920, 1080, Layer.BACKGROUND);
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

        try {
            ImageEntity background = new ImageEntity("ils-logo", IdGenerator.generateId(),
                    ImageIO.read(new File("res/logos/ils-logo.png")), 1750, 950, 270/2, 140/2, Layer.UI);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        TextBody playerName = new TextBody("profile name", IdGenerator.generateId(),
                120, 970, 300, 64, FontCollection.bit8FontLarge, Bit8.DARK_GREY, Game.config().getProfile().profile
        );
        addEntityToScene(playerName);
        //TODO: add button to change name
        GenericButton edit = null;
        try {
            edit = new GenericButton(
                    "edit", IdGenerator.generateId(),
                    50, 950, 64, 64,
                    new ChangeNameAction(playerName),
                    ImageIO.read(new File("res/menus/gui/pen.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(edit);
    }
}
