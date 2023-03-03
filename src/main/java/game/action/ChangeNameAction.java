package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;

import javax.swing.*;
import java.awt.*;

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
