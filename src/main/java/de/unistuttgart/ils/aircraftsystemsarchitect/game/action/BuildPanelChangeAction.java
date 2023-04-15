package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base.BaseGameFieldScene;

/**
 * An action for changing the build panel in the game.
 */
public class BuildPanelChangeAction extends Action {
    private final BuildPanelChange buildPanelChange;

    /**
     * Creates a new BuildPanelChangeAction instance with the specified panel change.
     *
     * @param buildPanelChange The change to apply to the build panel.
     */
    public BuildPanelChangeAction(BuildPanelChange buildPanelChange) {
        this.buildPanelChange = buildPanelChange;
    }

    /**
     * Handles the build panel change action by updating the game scene's build panel page.
     */
    @Override
    public void handle() {
        if (Game.scene().current() instanceof BaseGameFieldScene gs) {
            if (buildPanelChange == BuildPanelChange.LEFT) {
                gs.prevBuildPanelPage();
            } else if (buildPanelChange == BuildPanelChange.RIGHT) {
                gs.nextBuildPanelPage();
            }
        }
    }
}
