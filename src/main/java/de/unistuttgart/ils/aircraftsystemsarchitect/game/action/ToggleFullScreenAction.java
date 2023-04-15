package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;

/**
 * Switch to the opposite state fullscreen <-> window mode
 */
public class ToggleFullScreenAction extends Action {

    /**
     * Toggles the screen between fullscreen and windowed mode.
     */
    @Override
    public void handle() {
        Game.config().renderConfiguration().setFullscreenMode(!Game.config().renderConfiguration().getFullscreenMode());
        Game.frame().toggleFullScreen();
    }
}
