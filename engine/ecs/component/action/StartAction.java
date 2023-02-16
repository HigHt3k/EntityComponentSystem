package engine.ecs.component.action;

import engine.Game;
import engine.graphics.scene.Scene;

public class StartAction extends Action {
    private Scene scene;

    public StartAction(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void handle() {
        System.out.println("set scene to: " + scene.getName());
        Game.scene().setCurrentScene(scene);
        Game.graphics().recollectEntities();
    }
}
