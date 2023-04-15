package de.unistuttgart.ils.aircraftsystemsarchitect.game.config;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.config.Configuration;

/**
 * A configuration file for TDW specific things
 */
public class TDWConfiguration extends Configuration {

    private boolean tdwConfigIsActive = false;

    /**
     * Checks if the TDW configuration is active.
     *
     * @return true if TDW configuration is active, false otherwise
     */
    public boolean isTdwConfigIsActive() {
        return tdwConfigIsActive;
    }

    /**
     * Sets whether the TDW configuration should be active or not.
     *
     * @param tdwConfigIsActive true to activate the TDW configuration, false to deactivate
     */
    public void setTdwConfigIsActive(boolean tdwConfigIsActive) {
        this.tdwConfigIsActive = tdwConfigIsActive;
    }
}
