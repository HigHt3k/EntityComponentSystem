package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene.Scene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.menu.LevelMenuScene;

/**
 * Scene switching action. Stores both an id and / or a scene, so both may be used.
 */
public class StartAction extends Action {
    private Scene scene;
    private int id = -1;

    /**
     * set up the start action using a scene object
     * @param scene
     */
    public StartAction(Scene scene) {
        this.scene = scene;
    }

    /**
     * set up the start action using a scene id
     * @param id
     */
    public StartAction(int id) {
        this.id = id;
    }

    /**
     * switch the scene, depending on scene being available or id being available.
     */
    @Override
    public void handle() {
        Game.res().score().loadScores("res/scores/highscores.xml");
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
