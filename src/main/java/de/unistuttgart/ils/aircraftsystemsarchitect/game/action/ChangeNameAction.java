package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import javax.swing.*;

/**
 * Action that prompts the user to enter a new name and changes the name of the player profile accordingly
 */
public class ChangeNameAction extends Action {
    Entity playerProfile;

    public ChangeNameAction(Entity playerProfile) {
        this.playerProfile = playerProfile;
    }

    /**
     * Prompt the user to enter a new name and change the name of the player profile accordingly
     */
    @Override
    public void handle() {
        String name = JOptionPane.showInputDialog("Enter name:");
        Game.config().getProfile().profile = name;
        playerProfile.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(name);
    }
}
