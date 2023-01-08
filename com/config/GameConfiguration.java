package com.config;


public class GameConfiguration {
    private static final RenderConfiguration renderConfiguration = new RenderConfiguration();
    private boolean debug = false;
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

}
