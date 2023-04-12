package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.config.Configuration;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ExitAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.config.TDWConfiguration;

public class CustomExitAction extends ExitAction {

    @Override
    public void handle() {
        for(Configuration c : Game.config().getCustomConfigurations()) {
            if(c.getClass() == TDWConfiguration.class) {
                if (!((TDWConfiguration) c).isTdwConfigIsActive()) {
                    super.handle();
                } else if(Game.scene().current().getId() == -255) {
                    super.handle();
                }

                return;
            }
        }

        super.handle();
    }
}
