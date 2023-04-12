package de.unistuttgart.ils.aircraftsystemsarchitect.engine.config;

/**
 * Configuration of sound, contains information about audio status
 */
public class SoundConfiguration extends Configuration {
    private boolean isMuted = false;

    /**
     * check if the audio should be muted
     * @return true if muted, false if not muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * set the current audio status
     * @param muted: true if muted, false if unmuted
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}
