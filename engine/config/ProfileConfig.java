package engine.config;

import engine.resource.profile.NameCollection;

public class ProfileConfig {

    public String profile;

    public ProfileConfig() {
        profile = NameCollection.buildName();
        // TODO: No 2 same names!
    }
}
