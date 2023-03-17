package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;

/**
 * Mute the audio system
 */
public class MuteAction extends Action {
    @Override
    public void handle() {
        Game.config().getSound().setMuted(!Game.config().getSound().isMuted());
    }
}
