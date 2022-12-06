package com.graphics.render;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageRenderer {
    //TODO: implement
    public static void render(Graphics2D g, BufferedImage img, int x, int y, int width, int height) {
        g.drawImage(img, x, y, width, height, null);
    }
}
