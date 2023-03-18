package de.unistuttgart.ils.skyengine.ecs.component.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.graphics.scene.Scene;
import de.unistuttgart.ils.skylogic.scenes.game.GameScene;
import de.unistuttgart.ils.skylogic.scenes.menu.LevelMenuScene;

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
            if(id == -254) {
                ((LevelMenuScene) Game.scene().getScene(id)).checkUnlocks();
            }
            Game.scene().setCurrentScene(id);
        }
    }
}
