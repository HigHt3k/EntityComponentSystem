package de.unistuttgart.ils.skylogic.customexceptions;

@Deprecated
public class ConnectionException extends Exception {

    public ConnectionException(String errorMessage) {
        super(errorMessage);
    }
}
