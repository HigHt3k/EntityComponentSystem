package engine.config;

import engine.resource.lang.LanguageType;

public class GameConfiguration {
    private static final RenderConfiguration renderConfiguration = new RenderConfiguration();
    private boolean debug = false;
    private LanguageType language = LanguageType.EN_US;
    private static final ControlsConfig controls = new ControlsConfig();
    private static final ProfileConfig profile = new ProfileConfig();
    //TODO: what other configurations are needed? Language?

    public GameConfiguration() {

    }

    public ControlsConfig getControls() {
        return controls;
    }

    public RenderConfiguration renderConfiguration() {
        return renderConfiguration;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setLanguage(LanguageType language) {
        this.language = language;
    }

    public LanguageType getLanguage() {
        return language;
    }

    public ProfileConfig getProfile() {
        return profile;
    }
}
