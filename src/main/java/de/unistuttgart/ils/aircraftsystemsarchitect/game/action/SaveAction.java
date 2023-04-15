package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;

/**
 * Saves the current level to disk.
 */
public class SaveAction extends Action {

    /**
     * Saves the current level to disk.
     */
    @Override
    public void handle() {
        Game.res().saveLevel(Game.scene().current());
    }
}
