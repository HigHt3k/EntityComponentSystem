package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import game.scenes.GameScene;

public class BuildPanelChangeAction extends Action {
    private BuildPanelChange buildPanelChange;

    public BuildPanelChangeAction(BuildPanelChange buildPanelChange) {
        this.buildPanelChange = buildPanelChange;
    }

    @Override
    public void handle() {
        if (Game.scene().current() instanceof GameScene gs) {
            if (buildPanelChange == BuildPanelChange.LEFT) {
                gs.prevBuildPanelPage();
            } else if (buildPanelChange == BuildPanelChange.RIGHT) {
                gs.nextBuildPanelPage();
            }
        }
    }
}
