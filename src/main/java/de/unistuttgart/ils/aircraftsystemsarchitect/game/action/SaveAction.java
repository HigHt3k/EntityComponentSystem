package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;

public class SaveAction extends Action {
    @Override
    public void handle() {
        Game.res().saveLevel(Game.scene().current());
    }
}
