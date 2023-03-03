package engine.ecs.component.action;

import engine.Game;
import engine.graphics.scene.Scene;
import game.scenes.GameScene;

public class StartAction extends Action {
    private Scene scene;
    private int id = -1;

    public StartAction(Scene scene) {
        this.scene = scene;
    }

    public StartAction(int id) {
        this.id = id;
    }

    @Override
    public void handle() {
        if (scene != null) {
            if(scene instanceof GameScene gs) {
                if(gs.isUnlocked()) {
                    System.out.println("set scene to: " + scene.getName());
                    Game.scene().setCurrentScene(scene);
                }
            } else {
                System.out.println("set scene to: " + scene.getName());
                Game.scene().setCurrentScene(scene);
            }

        } else if (id != -1) {
            Game.scene().setCurrentScene(id);
        }
    }
}