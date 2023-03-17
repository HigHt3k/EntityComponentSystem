package de.unistuttgart.ils.skylogic.scenes.tutorial;

/**
 * Implement this interface, whenever a tutorial is needed in a scene. Simply overwrite the methods with
 * the tutorial content
 */
public interface Tutorial {

    void tutorialDialogue();
    void setTutorialRunning();
    boolean isTutorialRunning();
    void showNextTutorialText();
    void removeTutorial();
}
