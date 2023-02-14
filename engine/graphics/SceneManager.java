package engine.graphics;

import engine.graphics.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all scenes available to the game. SceneManager is used to set the currently active scene, which can be main menu, options, level, level overview, ...
 */
public class SceneManager {
    private List<Scene> scenes;

    private Scene currentScene;

    public SceneManager() {
        scenes = new ArrayList<>();
    }

    /**
     * Gives the currently active scene.
     * @return active scene
     */
    public Scene current() {
        return currentScene;
    }

    /**
     * adds a scene to the scene stack available to the game
     * @param scene: new scene to be added
     */
    public void addScene(Scene scene) {
        scenes.add(scene);
    }

    /**
     * sets the currently active scene to a new current scene by passing a scene name.
     * Will call the scene stack to search for the scene, which will be passed to @see setCurrentScene(Scene currentScene)
     * @param name: scene name, case-insensitive.
     */
    public void setCurrentScene(String name) {
        for (Scene s: scenes) {
            if(s.getName().equalsIgnoreCase(name)) {
                setCurrentScene(s);
                return;
            }
        }
    }

    /**
     * sets the currently active scene to a new current scene by passing a scene name.
     * Will call the scene stack to search for the scene, which will be passed to @see setCurrentScene(Scene currentScene)
     * @param id: scene id
     */
    public void setCurrentScene(int id) {
        for (Scene s : scenes) {
            if(s.getId() == id) {
                setCurrentScene(s);
                return;
            }
        }
    }

    /**
     * sets the currently active scene to a new current scene directly by passing the scene
     * @param currentScene: scene to be active
     */
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public int getSceneAmount() {
        return scenes.size();
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public Scene getScene(int id) {
        for(Scene s : scenes) {
            if(s.getId() == id) {
                return s;
            }
        }
        return null;
    }
}
