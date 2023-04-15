package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.BuildComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation.BuildPanelEntity;

/**
 * Action to set the amount of an entity to be built or added
 */
public class SetAmountAction extends Action {
    private BuildPanelEntity b;
    private int amount;

    /**
     * Creates a new SetAmountAction instance
     *
     * @param b The BuildPanelEntity for which the amount should be set
     * @param amount The new amount to be set
     */
    public SetAmountAction(BuildPanelEntity b, int amount) {
        this.b = b;
        this.amount = amount;
    }

    /**
     * Handles the action of setting the amount of an entity to be built or added
     */
    @Override
    public void handle() {
        System.out.println("handling action");
        b.getComponent(BuildComponent.class).setAmount(b.getComponent(BuildComponent.class).getAmount() + amount);
        b.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)
                .setText(String.valueOf(b.getComponent(BuildComponent.class).getAmount()));
    }
}
