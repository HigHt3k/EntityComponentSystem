package game.entities;

import engine.ecs.component.action.Action;
import engine.ecs.entity.GenericButton;
import engine.resource.colorpalettes.Bit8;

import java.awt.*;

public class LevelButton extends GenericButton {
    private Color difficultyColor;
    private Color lockedColor = Bit8.LIGHT_GREY;
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
    public LevelButton(String name, int id, int x, int y, int width, int height, String text, Font font, Color c, Action action) {
        super(name, id, x, y, width, height, text, font, action);
        this.button.borderColor = lockedColor;
        this.button.fillColor = lockedColor;
        this.difficultyColor = c;
    }

    public void unlock() {
        this.button.fillColor = difficultyColor;
        this.button.borderColor = difficultyColor;
    }
}
