package com.graphics.render;

import com.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScalingEngine {
    private static float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static float scaleH = Game.config().renderConfiguration().getScaleHeight();
    AffineTransform af = new AffineTransform();

    public ScalingEngine() {
        af.scale(scaleW, scaleH);
    }

    public Point scalePoint(Point p) {
        return new Point(scaleX(p.getX()), scaleY(p.getY()));
    }

    public int scaleX(double x) {
        return (int) Math.round(x * scaleW);
    }

    public int scaleY(double y) {
        return (int) Math.round(y * scaleH);
    }

    public Rectangle scaleRect(Rectangle r) {
        return new Rectangle(scaleX(r.getX()), scaleY(r.getY()), scaleX(r.getWidth()), scaleY(r.getHeight()));
    }

    public Shape scaleShape(Shape s) {
        return af.createTransformedShape(s);
    }

    public Font scaleFont(Font f) {
        return f.deriveFont(scaleW * f.getSize());
    }
}
