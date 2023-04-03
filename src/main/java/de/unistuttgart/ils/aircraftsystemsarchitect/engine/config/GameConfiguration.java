package de.unistuttgart.ils.aircraftsystemsarchitect.engine.config;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.lang.LanguageType;

/**
 * Configurator class for different settings of the game.
 * Collection of references to other classes, e.g. rendering options, controls options, language settings, ...
 */
public class GameConfiguration {
    private static final RenderConfiguration renderConfiguration = new RenderConfiguration();
    private boolean debug = false;
    private LanguageType language = LanguageType.EN_US;
    private static final ControlsConfig controls = new ControlsConfig();
    private static final ProfileConfig profile = new ProfileConfig();
    private static final SoundConfiguration sound = new SoundConfiguration();

    /**
     * get the sound configuration for volume, mute, ...
     * @return sound configuration
     */
    public SoundConfiguration getSound() {
        return sound;
    }

    /**
     * get the controls configuration (for controller / gamepad and mouse settings)
     * @return controls configuration
     */
    public ControlsConfig getControls() {
        return controls;
    }

    /**
     * get the render configuration, used for aliasing options, dimensions, scaling
     * @return
     */
    public RenderConfiguration renderConfiguration() {
        return renderConfiguration;
    }

    /**
     * set debugging mode
     * @param debug: true if on, false if off
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * check for debugging mode
     * @return true if on, false if off
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * set the currently used language
     * @param language: language to be used
     */
    public void setLanguage(LanguageType language) {
        this.language = language;
    }

    /**
     * get the currently used language
     * @return: language type
     */
    public LanguageType getLanguage() {
        return language;
    }

    /**
     * get the profile config, used to properly store scores
     * @return the profile config
     */
    public ProfileConfig getProfile() {
        return profile;
    }
}
