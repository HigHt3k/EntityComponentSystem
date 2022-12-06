package game.customexceptions;

public class ConnectionException extends Exception {

    public ConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
