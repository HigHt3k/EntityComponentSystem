package de.unistuttgart.ils.skyengine.ecs.component.graphics.objects;

import java.awt.*;

public class AnimationObject extends RenderObject {
    private final Image animation;
    private long curFrame = 0;
    private boolean randomize = false;
    private long pauseFrames = 0;
    private boolean paused = false;

    public AnimationObject(Point location, Shape bounds, Layer layer, Image animation) {
        super(location, bounds, layer);
        this.animation = animation;
    }

    public AnimationObject(Point location, Shape bounds, Layer layer, Image animation, boolean randomize, long pauseFrames) {
        super(location, bounds, layer);
        this.animation = animation;
        this.randomize = randomize;
        this.pauseFrames = pauseFrames;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Image getAnimation() {
        return animation;
    }

    public long getCurFrame() {
        return curFrame;
    }

    public void setCurFrame(long curFrame) {
        this.curFrame = curFrame;
    }

    public long getPauseFrames() {
        return pauseFrames;
    }

    public boolean isRandomize() {
        return randomize;
    }
}
