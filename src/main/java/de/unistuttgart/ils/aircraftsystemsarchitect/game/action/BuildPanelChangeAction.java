package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base.BaseGameFieldScene;

public class BuildPanelChangeAction extends Action {
    private final BuildPanelChange buildPanelChange;

    public BuildPanelChangeAction(BuildPanelChange buildPanelChange) {
        this.buildPanelChange = buildPanelChange;
    }

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
