package com.graphics.render;

import java.awt.*;

public class ShapeRenderer {

    public static void render(Graphics2D g, Shape shape, Color borderColor, Color fillColor) {
        if(shape == null) {
            return;
        }
        if(fillColor != null) {
            g.setColor(fillColor);
            g.fill(shape);
        }
        if(borderColor != null) {
            g.setColor(borderColor);
            g.draw(shape);
        }
    }
}
