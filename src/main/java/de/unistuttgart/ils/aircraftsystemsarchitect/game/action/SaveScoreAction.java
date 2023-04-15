package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score.HighScore;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;

/**
 * Action that saves the player's score to the high score list
 */
public class SaveScoreAction extends Action {

    /**
     * Saves the player's score to the high score list
     */
    @Override
    public void handle() {
        if (Game.scene().current() instanceof GameScene gs) {
            Game.res().score().addScore(new HighScore(Game.config().getProfile().profile, gs.getScore(), gs.getId()));
        }
    }
}
