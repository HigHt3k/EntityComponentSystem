package com.graphics.render;

import java.awt.*;

/**
 * Does the rendering of Strings. Different possibilities to render text, e.g. with specific font, color or rotation
 */
public class TextRenderer {
    public static Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);

    /**
     * renders a text specified by color with default font
     * @param g: graphics context
     * @param text: text to render
     * @param color: color of text
     * @param location: location on screen
     */
    public static void render(Graphics2D g, String text, Color color, Point location) {
        g.setColor(color);
        g.setFont(DEFAULT_FONT);

        for (String line : text.split("\n"))
            g.drawString(line, location.x, location.y += g.getFontMetrics().getHeight());
    }

    /**
     * renders a text specified by color and font
     * @param g: graphics context
     * @param text: text to render
     * @param color: color of text
     * @param font: font type
     * @param location: location on screen
     */
    public static void render(Graphics2D g, String text, Color color, Font font, Point location) {
        g.setFont(font);
        g.setColor(color);

        for (String line : text.split("\n"))
            g.drawString(line, location.x, location.y += g.getFontMetrics().getHeight());
    }

    /**
     * renders a text specified by color and rotation with the default font
     * @param g: graphics context
     * @param text: text to render
     * @param color: color of text
     * @param location: location on screen
     * @param rotation: rotation in degrees
     */
    public static void render(Graphics2D g, String text, Color color, Point location, float rotation) {
        g.setColor(color);
        g.setFont(DEFAULT_FONT);
        g.rotate(rotation);

        for (String line : text.split("\n"))
            g.drawString(line, location.x, location.y += g.getFontMetrics().getHeight());

        g.rotate(0);
    }

    /**
     * renders a text specified by color, font and rotation
     * @param g: graphics context
     * @param text: text to render
     * @param color: color of text
     * @param font: font type
     * @param location: location on screen
     * @param rotation: rotation
     */
    public static void render(Graphics2D g, String text, Color color, Font font, Point location, float rotation) {
        g.setColor(color);
        g.setFont(font);
        g.rotate(rotation);

        for (String line : text.split("\n"))
            g.drawString(line, location.x, location.y += g.getFontMetrics().getHeight());

        g.rotate(0);
    }
}
