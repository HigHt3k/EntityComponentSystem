package game.action;

import engine.Game;
import engine.ecs.component.action.Action;

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
