package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;

public class SaveAction extends Action {
    @Override
    public void handle() {
        Game.res().saveLevel(Game.scene().current());
    }
}
