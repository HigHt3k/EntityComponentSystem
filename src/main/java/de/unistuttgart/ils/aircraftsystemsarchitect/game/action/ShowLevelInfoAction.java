package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.menu.LevelMenuScene;

public class ShowLevelInfoAction extends Action {
    private final int levelId;

    public ShowLevelInfoAction(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public void handle() {
        if (Game.scene().current() instanceof LevelMenuScene ls) {
            ls.addHighscores(levelId);
            ls.showLevelInfo(levelId);
        }
    }
}
