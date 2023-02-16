package engine.ecs.entity;

import engine.ecs.component.action.ActionComponent;
import engine.ecs.component.action.StartAction;
import engine.graphics.scene.Scene;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SceneStartButton extends GenericButton {
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
    public SceneStartButton(String name, int id, int x, int y, int width, int height, String text, Font font, Scene scene) {
        super(name, id, x, y, width, height, text, font);
        ActionComponent actionComponent = new ActionComponent();
        actionComponent.addAction(MouseEvent.BUTTON1, new StartAction(scene));
        actionComponent.setEntity(this);
        this.addComponent(actionComponent);
    }
}
