package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.skyengine.ecs.entity.Entity;

import javax.swing.*;

public class ChangeNameAction extends Action {
    Entity playerProfile;

    public ChangeNameAction(Entity playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public void handle() {
        String name = JOptionPane.showInputDialog("Enter name:");
        Game.config().getProfile().profile = name;
        ((TextObject) playerProfile.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(name);
    }
}
