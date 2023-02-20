package engine.config;

import engine.Game;
import engine.profile.NameCollection;
import engine.resource.score.HighScore;

public class ProfileConfig {

    public String profile;

    public ProfileConfig() {
        profile = NameCollection.buildName();
        // TODO: No 2 same names!
    }
}
