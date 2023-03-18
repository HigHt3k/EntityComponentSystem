package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.SimulationSystem;

public class ValidateAction extends Action {

    @Override
    public void handle() {
        Game.system().getSystem(SimulationSystem.class).finish();
    }
}
