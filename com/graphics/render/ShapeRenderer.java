package com.graphics.render;

import java.awt.*;

public class ShapeRenderer {

    public static void render(Graphics2D g, Shape shape, Color color, boolean fill) {
        g.setColor(color);
        if(fill)
            g.fill(shape);
        else
            g.draw(shape);
    }
}
