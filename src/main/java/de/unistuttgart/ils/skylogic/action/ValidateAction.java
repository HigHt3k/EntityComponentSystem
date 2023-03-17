package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;
import de.unistuttgart.ils.skylogic.handler.SimulationSystem;

public class ValidateAction extends Action {

    @Override
    public void handle() {
        Game.system().getSystem(SimulationSystem.class).finish();
    }
}
