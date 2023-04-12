package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.config.Configuration;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.config.TDWConfiguration;

public class ToggleExitButtonActiveAction extends Action {
    @Override
    public void handle() {
        for(Configuration c : Game.config().getCustomConfigurations()) {
            if(c.getClass() == TDWConfiguration.class) {
                ((TDWConfiguration) c).setTdwConfigIsActive(!((TDWConfiguration) c).isTdwConfigIsActive());
            }
        }
    }
}
