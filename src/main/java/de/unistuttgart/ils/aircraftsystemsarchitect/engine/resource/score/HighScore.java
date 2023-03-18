package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score;

public class HighScore {
    private String name;
    private int score;
    private int levelId;

    public HighScore(String name, int score, int levelId) {
        this.name = name;
        this.score = score;
        this.levelId = levelId;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
}
