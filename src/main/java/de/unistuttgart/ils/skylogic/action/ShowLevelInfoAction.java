package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;
import de.unistuttgart.ils.skylogic.scenes.menu.LevelMenuScene;

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
