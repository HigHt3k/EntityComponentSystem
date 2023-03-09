package game.scenes.tutorial;

public interface Tutorial {

    void tutorialDialogue();
    void setTutorialRunning();
    boolean isTutorialRunning();
    void showNextTutorialText();
    void removeTutorial();
}
