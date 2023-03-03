package game.customexceptions;

@Deprecated
public class ConnectionException extends Exception {

    public ConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
