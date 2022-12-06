package com.config;


public class GameConfiguration {
    private static final RenderConfiguration renderConfiguration = new RenderConfiguration();
    //TODO: what other configurations are needed? Language?

    public GameConfiguration() {

    }

    public RenderConfiguration renderConfiguration() {
        return renderConfiguration;
    }

}
