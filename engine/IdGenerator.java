package engine;

public class IdGenerator {

    private static int id = 0;

    public static int generateId() {
        id += 1;
        return id;
    }
}
