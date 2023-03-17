package de.unistuttgart.ils.skyengine.ecs.component.action;

import de.unistuttgart.ils.skyengine.Game;

public class ExitAction extends Action {
    @Override
    public void handle() {
        Game.logger().info("Exiting game with normal state.");
        System.exit(1);
    }
}
