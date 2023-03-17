package de.unistuttgart.ils.skyengine.config;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.graphics.render.RenderingEngine;

import java.awt.*;

/**
 * contains information about default screen resolution, scaling, etc.
 */
public class RenderConfiguration {
    private final int DEFAULT_RESOLUTION_WIDTH = 1920;
    private final int DEFAULT_RESOLUTION_HEIGHT = 1080;
    private final float DEFAULT_SCREEN_RATIO = (float) DEFAULT_RESOLUTION_WIDTH/(float) DEFAULT_RESOLUTION_HEIGHT;
    private Dimension resolution;
    private float screenRatio;
    private float scaleWidth = 1.0f;
    private float scaleHeight = 1.0f;
    private final float frameRate = 244.0f;

    private boolean fullscreenMode = false;
    private final Object aliasingText = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
    private final Object aliasing = RenderingHints.VALUE_ANTIALIAS_ON;

    public float getFrameRate() {
        return frameRate;
    }

    /**
     * create a new RenderConfiguration. Sets the current screen size as resolution and scaling for this screen
     */
    public RenderConfiguration() {
        int width = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds().width;
        int height = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds().height;
        this.resolution = new Dimension(width, height);

        this.screenRatio = (float) resolution.width / (float) resolution.height;
        if(screenRatio > DEFAULT_SCREEN_RATIO) {
            this.resolution.width = (int) (this.resolution.height * DEFAULT_SCREEN_RATIO);
        } else if(screenRatio < DEFAULT_SCREEN_RATIO) {
            this.resolution.height = (int) (this.resolution.width /DEFAULT_SCREEN_RATIO);
        }
        scale();
        Game.logger().info("Screen size: " + this.resolution);
        Game.logger().info("Using scaling factors for width and height: "
                + scaleWidth + ", " + scaleHeight);
    }

    /**
     * Get screen Resolution
     * @return the resolution of the screen
     */
    public Dimension getResolution() {
        return resolution;
    }

    /**
     * apply the design screen size to the current screen size
     * and calculate scaling factors for @{@link RenderingEngine}
     */
    private void scale() {
        scaleWidth = (float) resolution.width / DEFAULT_RESOLUTION_WIDTH;
        scaleHeight = (float) resolution.height / DEFAULT_RESOLUTION_HEIGHT;
    }

    /**
     * @return scaling factor for width
     */
    public float getScaleWidth() {
        return scaleWidth;
    }

    /**
     * @return scaling factor for height
     */
    public float getScaleHeight() {
        return scaleHeight;
    }

    /**
     * @return true if is fullscreen or false if not
     */
    public boolean getFullscreenMode() {
        return fullscreenMode;
    }

    /**
     * toggle fullscreen mode
     * @param fullscreenMode: true if fullscreen, false if not
     */
    public void setFullscreenMode(boolean fullscreenMode) {
        this.fullscreenMode = fullscreenMode;
        //TODO: Implement fullscreen mode
    }

    /**
     * get the antialiasing mode for rendering graphics
     * @return the antialiasing mode
     */
    public Object getAntialiasing() {
        return aliasing;
    }

    /**
     * get the antialiasing mode for rendering text
     * @return the antialiasing mode
     */
    public Object getAliasingText() {
        return aliasingText;
    }

    /**
     * update dimension variable and scaling factors based on the current window size & fullscreen mode
     */
    public void resize() {
        int width = Game.frame().getWidth();
        int height = Game.frame().getHeight();
        this.resolution = new Dimension(width, height);

        this.screenRatio = (float) resolution.width / (float) resolution.height;
        if(screenRatio > DEFAULT_SCREEN_RATIO) {
            this.resolution.width = (int) (this.resolution.height * DEFAULT_SCREEN_RATIO);
        } else if(screenRatio < DEFAULT_SCREEN_RATIO) {
            this.resolution.height = (int) (this.resolution.width /DEFAULT_SCREEN_RATIO);
        }
        scale();
        Game.logger().info("Screen size: " + this.resolution);
        Game.logger().info("Using scaling factors for width and height: "
                + scaleWidth + ", " + scaleHeight);
    }
}
