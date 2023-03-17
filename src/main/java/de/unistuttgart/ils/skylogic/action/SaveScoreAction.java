package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;
import de.unistuttgart.ils.skyengine.resource.score.HighScore;
import de.unistuttgart.ils.skylogic.scenes.game.GameScene;

public class SaveScoreAction extends Action {

    @Override
    public void handle() {
        if (Game.scene().current() instanceof GameScene gs) {
            Game.res().score().addScore(new HighScore(Game.config().getProfile().profile, gs.getScore(), gs.getId()));
        }
    }
}
