package de.unistuttgart.ils.skylogic.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.action.Action;

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
