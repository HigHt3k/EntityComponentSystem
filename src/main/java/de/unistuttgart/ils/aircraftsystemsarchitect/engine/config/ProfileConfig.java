package de.unistuttgart.ils.aircraftsystemsarchitect.engine.config;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.profile.NameCollection;

/**
 * Profile configuration, contains information about the username
 */
public class ProfileConfig extends Configuration {

    public String profile;

    /**
     * generate a new profile, with a random name from a collection of given names
     */
    public ProfileConfig() {
        profile = NameCollection.buildName();
        // TODO: No 2 same names!
    }
}
