package engine.graphics.render;

import java.awt.*;

public class LineRenderer {

    public static void render(Graphics2D g, Point p1, Point p2, Color color, int thickness) {
        Stroke s = g.getStroke();
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
        g.setStroke(s);
    }

}
