package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import game.scenes.LevelScene;

public class ShowLevelInfoAction extends Action {
    private final int levelId;

    public ShowLevelInfoAction(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public void handle() {
        if (Game.scene().current() instanceof LevelScene ls) {
            System.out.println("Showing ls info");
            ls.addHighscores(levelId);
            ls.showLevelInfo(levelId);
        }
    }
}
