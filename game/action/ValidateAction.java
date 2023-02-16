package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import game.handler.SimulationSystem;

public class ValidateAction extends Action {

    @Override
    public void handle() {
        Game.system().getSystem(SimulationSystem.class).finish();
    }
}
