package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.SimulationSystem;

/**
 * Action for validating the simulation.
 */
public class ValidateAction extends Action {

    /**
     * Handles the validation of the simulation.
     * starts the simulation system and stops it after validation
     */
    @Override
    public void handle() {
        Game.system().getSystem(SimulationSystem.class).finish();
    }
}
