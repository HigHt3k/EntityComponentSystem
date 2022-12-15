package com.graphics.render;

import com.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScalingEngine {
    private static float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static float scaleH = Game.config().renderConfiguration().getScaleHeight();

    public static Point scalePoint(Point p) {
        return new Point(scaleX(p.getX()), scaleY(p.getY()));
    }

    public static int scaleX(double x) {
        return (int) (x * scaleW);
    }

    public static int scaleY(double y) {
        return (int) (y * scaleH);
    }

    public static Rectangle scaleRect(Rectangle r) {
        return new Rectangle(scaleX(r.getX()), scaleY(r.getY()), scaleX(r.getWidth()), scaleY(r.getHeight()));
    }

    public static Shape scaleShape(Shape s) {
        AffineTransform af = new AffineTransform();
        af.scale(scaleW, scaleH);
        return af.createTransformedShape(s);
    }

    public static Font scaleFont(Font f) {
        return f.deriveFont(scaleW * f.getSize());
    }
}
