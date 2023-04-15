package de.unistuttgart.ils.aircraftsystemsarchitect.engine;

/**
 * This class generates IDs for entities in the game.
 */
public class IdGenerator {

    /**
     * The current ID.
     */
    private static int id = 0;

    /**
     * Generates the next available integer ID.
     * @return the ID.
     */
    public static int generateId() {
        id += 1;
        return id;
    }
}
