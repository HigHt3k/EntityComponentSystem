package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.config.Configuration;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.config.TDWConfiguration;

/**
 * Action that toggles the active state of the exit button in the TDW configuration.
 */
public class ToggleExitButtonActiveAction extends Action {

    /**
     * Toggles the active state of the exit button in the TDW configuration.
     */
    @Override
    public void handle() {
        for(Configuration c : Game.config().getCustomConfigurations()) {
            if(c instanceof TDWConfiguration tdwConfig) {
                tdwConfig.setTdwConfigIsActive(!tdwConfig.isTdwConfigIsActive());
            }
        }
    }
}
