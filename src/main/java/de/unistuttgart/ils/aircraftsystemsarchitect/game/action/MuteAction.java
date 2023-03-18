package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;

/**
 * Mute the audio system
 */
public class MuteAction extends Action {
    @Override
    public void handle() {
        Game.config().getSound().setMuted(!Game.config().getSound().isMuted());
    }
}
