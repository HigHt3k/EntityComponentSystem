package engine.config;

import engine.resource.profile.NameCollection;

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
