package de.unistuttgart.ils.aircraftsystemsarchitect.engine;

/**
 * Contains meta information about the game, including the title, author, version number, and description.
 */
public class GameInformation {

    private String title;
    private String author;
    private String version;
    private String description;

    /**
     * Sets the title of the game.
     *
     * @param title the title of the game
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the title of the game.
     *
     * @return the title of the game
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of the game.
     *
     * @return the author of the game
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the version number of the game.
     *
     * @return the version number of the game
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the description of the game.
     *
     * @return the description of the game
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the author of the game.
     *
     * @param author the author of the game
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the version number of the game.
     *
     * @param version the version number of the game
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Sets the description of the game.
     *
     * @param description the description of the game
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
