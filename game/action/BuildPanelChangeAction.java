package game.action;

import engine.ecs.component.action.Action;

public class BuildPanelChangeAction extends Action {
    private BuildPanelChange buildPanelChange;

    public BuildPanelChangeAction(BuildPanelChange buildPanelChange) {
        this.buildPanelChange = buildPanelChange;
    }

    @Override
    public void handle() {

    }
}
