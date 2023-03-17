package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;
import de.unistuttgart.ils.skylogic.scenes.base.BaseGameFieldScene;

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
