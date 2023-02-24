package game.action;

import engine.Game;
import engine.ecs.component.action.Action;

/**
 * Mute the audio system
 */
public class MuteAction extends Action {
    @Override
    public void handle() {
        Game.config().getSound().setMuted(!Game.config().getSound().isMuted());
    }
}
