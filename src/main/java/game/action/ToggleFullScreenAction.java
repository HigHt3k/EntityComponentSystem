package game.action;

import engine.Game;
import engine.ecs.component.action.Action;

/**
 * Switch to the opposite state fullscreen <-> window mode
 */
public class ToggleFullScreenAction extends Action {

    @Override
    public void handle() {
        Game.config().renderConfiguration().setFullscreenMode(!Game.config().renderConfiguration().getFullscreenMode());
        Game.frame().toggleFullScreen();
    }
}
