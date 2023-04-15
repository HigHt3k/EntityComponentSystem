package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render;

import java.awt.*;

/**
 * Class responsible for rendering animations in the aircraft systems architect engine.
 */
public class AnimationRenderer {

    /**
     * Renders an animation image at the specified position and size.
     *
     * @param g         The Graphics2D object used for drawing.
     * @param animation The Image object representing the animation.
     * @param x         The x-coordinate of the top-left corner of the animation.
     * @param y         The y-coordinate of the top-left corner of the animation.
     * @param width     The width of the animation.
     * @param height    The height of the animation.
     */
    public static synchronized void renderAnimation(Graphics2D g, Image animation, int x, int y, int width, int height) {
        g.drawImage(animation, x, y, width, height, null);
    }

}
