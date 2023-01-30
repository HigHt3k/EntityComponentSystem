package com.config;


import com.resource.LanguageType;

public class GameConfiguration {
    private static final RenderConfiguration renderConfiguration = new RenderConfiguration();
    private boolean debug = false;
    private LanguageType language = LanguageType.EN_US;
    //TODO: what other configurations are needed? Language?

    public GameConfiguration() {

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

}
