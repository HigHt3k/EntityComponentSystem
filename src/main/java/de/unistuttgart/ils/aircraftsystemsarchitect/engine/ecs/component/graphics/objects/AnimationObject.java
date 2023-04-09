package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects;

import java.awt.*;

/**
 * AnimationObjects store parameters to render animations by using GIF files
 * Contains current frame, frames that should be paused during animation and the state of the animation
 */
public class AnimationObject extends RenderObject {
    private final Image animation;
    private long curFrame = 0;
    private boolean randomize = false;
    private long pauseFrames = 0;
    private boolean paused = false;

    /**
     * Create new AnimationObject
     * @param location: location to render the object to the screen
     * @param bounds: boundaries of the object
     * @param layer: rendering layer
     * @param animation: animation image (GIF File)
     */
    public AnimationObject(Point location, Shape bounds, Layer layer, Image animation) {
        super(location, bounds, layer);
        this.animation = animation;
    }

    /**
     *
     * @param location: location to render the object to the screen
     * @param bounds: boundaries of the object
     * @param layer: rendering layer
     * @param animation: animation image (GIF File)
     * @param randomize: randomize the animation rendering (pause between two cycles)
     * @param pauseFrames: amount of frames that should be paused
     */
    public AnimationObject(Point location, Shape bounds, Layer layer, Image animation, boolean randomize, long pauseFrames) {
        super(location, bounds, layer);
        this.animation = animation;
        this.randomize = randomize;
        this.pauseFrames = pauseFrames;
    }

    /**
     * check if the animation is currently in paused state
     * @return true of paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Set the animation state
     * @param paused: true if paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Get the Animation
     * @return the animation image
     */
    public Image getAnimation() {
        return animation;
    }

    /**
     * Get the current frame of the animation
     * @return the frame
     */
    public long getCurFrame() {
        return curFrame;
    }

    /**
     * Set the current frame of the animation
     * @param curFrame: the frame (integer)
     */
    public void setCurFrame(long curFrame) {
        this.curFrame = curFrame;
    }

    /**
     * Get the frames that should be paused in between rendering the animation
     * @return the amount of paused frames
     */
    public long getPauseFrames() {
        return pauseFrames;
    }

    /**
     * Check if the animation should play at randomized start points
     * @return true if randomized is on
     */
    public boolean isRandomize() {
        return randomize;
    }
}
