package engine;

/**
 * Generate IDs for entities
 */
public class IdGenerator {

    private static int id = 0;

    /**
     * Generate the next available integer id
     * @return the id
     */
    public static int generateId() {
        id += 1;
        return id;
    }
}
