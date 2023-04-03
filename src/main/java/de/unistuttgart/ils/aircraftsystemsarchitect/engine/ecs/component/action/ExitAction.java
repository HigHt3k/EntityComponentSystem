package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

/**
 * Exit the application
 */
public class ExitAction extends Action {
    /**
     * call this to exit the application
     */
    @Override
    public void handle() {
        Game.logger().info("Exiting game with normal state.");
        System.exit(1);
    }
}
