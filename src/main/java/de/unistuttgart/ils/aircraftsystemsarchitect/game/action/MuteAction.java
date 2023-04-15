package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;

/**
 * An Action that mutes or unmutes the audio system in the game configuration.
 */
public class MuteAction extends Action {

    /**
     * Mutes or unmutes the audio system in the game configuration.
     */
    @Override
    public void handle() {
        Game.config().getSound().setMuted(!Game.config().getSound().isMuted());
    }
}
