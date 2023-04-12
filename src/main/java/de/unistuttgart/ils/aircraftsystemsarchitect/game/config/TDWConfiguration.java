package de.unistuttgart.ils.aircraftsystemsarchitect.game.config;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.config.Configuration;

/**
 * A configuration file for TDW specific things
 */
public class TDWConfiguration extends Configuration {
    private boolean tdwConfigIsActive = false;

    public boolean isTdwConfigIsActive() {
        return tdwConfigIsActive;
    }

    public void setTdwConfigIsActive(boolean tdwConfigIsActive) {
        this.tdwConfigIsActive = tdwConfigIsActive;
    }
}
