package engine.ecs.component.action;

import engine.Game;

public class ExitAction extends Action {
    @Override
    public void handle() {
        Game.logger().info("Exiting game with normal state.");
        System.exit(1);
    }
}
