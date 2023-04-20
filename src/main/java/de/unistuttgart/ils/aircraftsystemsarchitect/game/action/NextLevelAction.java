package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;

/**
 * Action to switch to the next level in the game.
 */
public class NextLevelAction extends Action {

    /**
     * Handles the action of switching to the next level.
     * If there is a next level, it sets it as the current scene.
     * If there is no next scene, it does nothing.
     */
    @Override
    public void handle() {
        int curLevel = Game.scene().current().getId();
        int nextLevel = curLevel + 1;

        if(Game.scene().getScene(nextLevel) != null) {
            if(Game.scene().current() instanceof GameScene gs) {
                gs.setWasPlayed(true);
            }
            Game.scene().setCurrentScene(nextLevel);
        } else {
            // Handle if no next scene
        }
    }
}
