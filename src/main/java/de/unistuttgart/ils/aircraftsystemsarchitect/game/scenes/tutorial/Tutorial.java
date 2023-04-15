package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.tutorial;

/**
 * Implement this interface, whenever a tutorial is needed in a scene.
 * The interface defines a set of methods that need to be implemented by a class.
 */
public interface Tutorial {

    /**
     * Displays the tutorial dialogue.
     */
    void tutorialDialogue();

    /**
     * Sets the tutorial to running state.
     */
    void setTutorialRunning();

    /**
     * Returns true if the tutorial is running, false otherwise.
     *
     * @return true if the tutorial is running, false otherwise.
     */
    boolean isTutorialRunning();

    /**
     * Displays the next tutorial text.
     */
    void showNextTutorialText();

    /**
     * Removes the tutorial.
     */
    void removeTutorial();
}
