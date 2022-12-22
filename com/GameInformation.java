package com;

/**
 * Contains meta information about the game [title, author, version number and description]
 */
public class GameInformation {
    private String title;
    private String author;
    private String version;
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
