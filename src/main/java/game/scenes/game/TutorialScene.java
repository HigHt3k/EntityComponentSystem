package game.scenes.game;

import engine.Game;
import game.handler.tutorial.TutorialHandler;
import game.scenes.tutorial.Tutorial;
import game.scenes.util.Difficulty;

public class TutorialScene extends GameScene implements Tutorial {
    private boolean firstTimeOpened = true;
    private boolean tutorialRunning = false;
    /**
     * create a new Tutorial Scene object;
     *
     * @param name
     * @param id
     * @param difficulty
     */
    public TutorialScene(String name, int id, Difficulty difficulty) {
        super(name, id, difficulty);
    }

    @Override
    public void init() {
        super.init();
        if(firstTimeOpened) {
            tutorialDialogue();
            firstTimeOpened = false;
        }
    }

    @Override
    public void tutorialDialogue() {
        tutorialRunning = true;
        Game.input().addHandler(new TutorialHandler());

    }

    @Override
    public void setTutorialRunning() {

    }

    @Override
    public boolean isTutorialRunning() {
        return tutorialRunning;
    }

    @Override
    public void showNextTutorialText() {

    }

    @Override
    public void removeTutorial() {

    }
}
