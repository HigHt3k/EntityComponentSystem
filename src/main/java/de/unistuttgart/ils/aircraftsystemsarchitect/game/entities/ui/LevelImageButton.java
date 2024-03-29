package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.GenericButton;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A image button used to display a level on the level map
 */
public class LevelImageButton extends GenericButton {
    private final Color lockedColor = Bit8.LIGHT_GREY;

    /**
     * Create a generic functional button. addIntent needs to be used to add a purpose other than hovering to this button.
     *
     * @param name   : Name of the entity
     * @param id     : Id of the entity
     * @param x      : x position by design
     * @param y      : y position by design
     * @param width  : design width
     * @param height : design height
     * @param text   : text to display (Centered) at the button
     * @param font   : Font to use for this button
     */
    public LevelImageButton(String name, int id, int x, int y, int width, int height, String text, Font font, Action action, BufferedImage image) {
        super(name, id, x, y, width, height, text, font, action);
        this.button.setHidden(false);
        this.button.borderColor = lockedColor;
        this.button.fillColor = lockedColor;

        this.getComponent(RenderComponent.class).addRenderObject(new ImageObject(new Point(x, y), new Rectangle(x, y, width, height), Layer.UI, image));
    }

    /**
     * unlock the button
     */
    public void unlock() {
        this.button.setHidden(true);
    }
}
