package game.action;

import engine.Game;
import engine.ecs.component.action.Action;

public class SaveAction extends Action {
    @Override
    public void handle() {
        Game.res().saveLevel(Game.scene().current());
    }
}
