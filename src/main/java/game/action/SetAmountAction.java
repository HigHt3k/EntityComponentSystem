package game.action;

import engine.ecs.component.action.Action;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.TextObject;
import game.components.BuildComponent;
import game.entities.simulation.BuildPanelEntity;

public class SetAmountAction extends Action {
    BuildPanelEntity b;
    int amount;

    public SetAmountAction(BuildPanelEntity b, int amount) {
        this.b = b;
        this.amount = amount;
    }

    @Override
    public void handle() {
        System.out.println("handling action");
        b.getComponent(BuildComponent.class).setAmount(b.getComponent(BuildComponent.class).getAmount() + amount);
        ((TextObject) b.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0))
                .setText(String.valueOf(b.getComponent(BuildComponent.class).getAmount()));
    }
}
