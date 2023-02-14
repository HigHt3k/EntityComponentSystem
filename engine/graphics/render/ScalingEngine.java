package engine.graphics.render;

import engine.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScalingEngine {
    private static float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static float scaleH = Game.config().renderConfiguration().getScaleHeight();
    AffineTransform af = new AffineTransform();
    AffineTransform upscaleAf = new AffineTransform();

    public ScalingEngine() {
        af.scale(scaleW, scaleH);
        upscaleAf.scale(1/scaleW, 1/scaleH);
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

    public Shape upscaleShape(Shape s) {
        return upscaleAf.createTransformedShape(s);
    }

    public Font scaleFont(Font f) {
        return f.deriveFont(scaleW * f.getSize());
    }

    public int upscaleX(double x) {
        return (int) Math.round(x * 1 / scaleW);
    }

    public int upscaleY(double y) {
        return (int) Math.round(y * 1 / scaleH);
    }

    public Point upscalePoint(Point p) {
        return new Point(upscaleX(p.getX()), upscaleY(p.getY()));
    }

    public Rectangle upscaleRect(Rectangle r) {
        return new Rectangle(upscaleX(r.getX()), upscaleY(r.getY()), upscaleX(r.getWidth()), upscaleY(r.getHeight()));
    }
}
