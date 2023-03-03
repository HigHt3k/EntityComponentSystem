package engine.graphics.render;

import java.awt.*;

/**
 * Renders Shapes to the screen using Graphics2D
 */
public class ShapeRenderer {

    /**
     * Render any kind of shape object to the screen with specified colors and outline details. If any value except the shape
     * is set to null, it will not be considered.
     * @param g: The graphics context
     * @param shape: The shape to render
     * @param borderColor: border color of the shape
     * @param fillColor: fill color of the shape
     * @param thickness: border thickness of the snape
     */
    public static synchronized void render(Graphics2D g, Shape shape, Color borderColor, Color fillColor, int thickness) {
        if(shape == null) {
            return;
        }
        if(fillColor != null) {
            g.setColor(fillColor);
            g.fill(shape);
        }
        if(borderColor != null) {
            g.setStroke(new BasicStroke(thickness));
            g.setColor(borderColor);
            g.draw(shape);
        }
    }
}
