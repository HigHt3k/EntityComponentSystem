package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import engine.resource.score.HighScore;
import game.scenes.GameScene;

public class SaveScoreAction extends Action {

    @Override
    public void handle() {
        if (Game.scene().current() instanceof GameScene gs) {
            Game.res().score().addScore(new HighScore(Game.config().getProfile().profile, gs.getScore(), gs.getId()));
        }
    }
}
