package de.unistuttgart.ils.aircraftsystemsarchitect.engine.config;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.profile.NameCollection;

/**
 * Profile configuration, contains information about the username
 */
public class ProfileConfig {

    public String profile;

    public ProfileConfig() {
        profile = NameCollection.buildName();
        // TODO: No 2 same names!
    }
}
