package engine.config;

/**
 * Configuration of controls of external devices.
 */
public class ControlsConfig {
    private final double cursorSpeed = 8f;
    private final double controllerSpeed = 2f;

    /**
     * get the default cursor speed
     * TODO: implement sensitivity options
     * @return: default cursor speed
     */
    public double getCursorSpeed() {
        return cursorSpeed;
    }

    /**
     * get the default controller speed
     * TODO: implement sensitivity options
     * @return default controller speed
     */
    public double getControllerSpeed() {
        return controllerSpeed;
    }
}
