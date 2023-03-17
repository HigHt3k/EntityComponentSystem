package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;

public class NextLevelAction extends Action {
    @Override
    public void handle() {
        int curLevel = Game.scene().current().getId();
        int nextLevel = curLevel + 1;

        if(Game.scene().getScene(nextLevel) != null) {
            Game.scene().setCurrentScene(nextLevel);
        } else {
            // Handle if no next scene
        }
    }
}
