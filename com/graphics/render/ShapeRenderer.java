package com.graphics.render;

import java.awt.*;

public class ShapeRenderer {

    public static void render(Graphics2D g, Shape shape, Color borderColor, Color fillColor, int thickness) {
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
