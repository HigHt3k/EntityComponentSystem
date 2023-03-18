package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

public class ExitAction extends Action {
    @Override
    public void handle() {
        Game.logger().info("Exiting game with normal state.");
        System.exit(1);
    }
}
