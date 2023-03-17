package de.unistuttgart.ils.skyengine.graphics.render;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Render images to the screen using Graphics2D
 */
public class ImageRenderer {

    /**
     * Render image to the graphics context at a given position
     * @param g: the graphics context
     * @param img: the image to render
     * @param x: x coordinate of the image
     * @param y: y coordinate of the image
     * @param width: width of the image
     * @param height: height of the image
     */
    public static synchronized void render(Graphics2D g, BufferedImage img, int x, int y, int width, int height) {
        g.drawImage(img, x, y, width, height, null);
    }
}
