package engine.graphics.render;

import java.awt.*;

public class AnimationRenderer {

    public static synchronized void renderAnimation(Graphics2D g, Image animation, int x, int y, int width, int height) {
        g.drawImage(animation, x, y, width, height, null);
    }

}
