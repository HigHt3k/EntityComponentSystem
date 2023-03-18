package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render;

import java.awt.*;

/**
 * Render lines (from Point A to B) to the screen using Graphics2D
 */
public class LineRenderer {

    /**
     * Render a line for two given points to the graphics context.
     * @param g: The graphics context
     * @param p1: Starting Point
     * @param p2: Ending Point
     * @param color: Line Color
     * @param thickness: Line thickness in px
     */
    public static synchronized void render(Graphics2D g, Point p1, Point p2, Color color, int thickness) {
        Stroke s = g.getStroke();
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
        g.setStroke(s);
    }

}
