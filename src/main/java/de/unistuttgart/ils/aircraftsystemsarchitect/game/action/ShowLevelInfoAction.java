package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.menu.LevelMenuScene;

/**
 * An action to show information about a certain level
 */
public class ShowLevelInfoAction extends Action {

    private final int levelId;

    /**
     * Constructs a new instance of the ShowLevelInfoAction class
     *
     * @param levelId the id of the level to show information about
     */
    public ShowLevelInfoAction(int levelId) {
        this.levelId = levelId;
    }

    /**
     * Handles the ShowLevelInfoAction by showing information about the corresponding level
     */
    @Override
    public void handle() {
        if (Game.scene().current() instanceof LevelMenuScene ls) {
            ls.addHighscores(levelId);
            ls.showLevelInfo(levelId);
        }
    }
}
