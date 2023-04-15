package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score;

/**
 * Represents a high score entry for a particular level in the game.
 */
public class HighScore {
    private String name;
    private int score;
    private int levelId;

    /**
     * Constructs a new HighScore instance with the provided name, score, and level ID.
     *
     * @param name    The name of the player who achieved the high score.
     * @param score   The score achieved.
     * @param levelId The ID of the level in which the score was achieved.
     */
    public HighScore(String name, int score, int levelId) {
        this.name = name;
        this.score = score;
        this.levelId = levelId;
    }

    /**
     * Retrieves the score achieved.
     *
     * @return The score as an integer.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the name of the player who achieved the high score.
     *
     * @return The player's name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the score achieved.
     *
     * @param score The new score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the name of the player who achieved the high score.
     *
     * @param name The new player name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the ID of the level in which the high score was achieved.
     *
     * @return The level ID as an integer.
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * Sets the ID of the level in which the high score was achieved.
     *
     * @param levelId The new level ID to set.
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
}
