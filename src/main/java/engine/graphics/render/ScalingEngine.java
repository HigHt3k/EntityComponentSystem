package engine.graphics.render;

import engine.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * The ScalingEngine is used to flexibly adapt all rendered objects to the screen size.
 */
public class ScalingEngine {
    private static float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static float scaleH = Game.config().renderConfiguration().getScaleHeight();
    AffineTransform af = new AffineTransform();
    AffineTransform upscaleAf = new AffineTransform();

    /**
     * Create a new ScalingEngine, automatically creates affine transform objects that will be used for shape scaling.
     */
    public ScalingEngine() {
        af.scale(scaleW, scaleH);
        upscaleAf.scale(1/scaleW, 1/scaleH);
    }

    /**
     * Scale a point to the screen size
     * @param p: the point to be scaled
     * @return: the scaled point
     */
    public Point scalePoint(Point p) {
        return new Point(scaleX(p.getX()), scaleY(p.getY()));
    }

    /**
     * Scale an x coordinate to the screen size
     * @param x: the value to be scaled
     * @return the scaled value
     */
    public int scaleX(double x) {
        return (int) Math.round(x * scaleW);
    }

    /**
     * Scale a y-coordinate to the screen size
     * @param y: the value to be scaled
     * @return the scaled value
     */
    public int scaleY(double y) {
        return (int) Math.round(y * scaleH);
    }

    /**
     * Scale a rectangle shape to the screen size
     * @param r: the rectangle to scale
     * @return scaled rectangle
     */
    public Rectangle scaleRect(Rectangle r) {
        return new Rectangle(scaleX(r.getX()), scaleY(r.getY()), scaleX(r.getWidth()), scaleY(r.getHeight()));
    }

    /**
     * Scale any form of shape to the screen size using AffineTransformation
     * @param s: the shape to be scaled
     * @return the scaled shape
     */
    public Shape scaleShape(Shape s) {
        return af.createTransformedShape(s);
    }

    /**
     * Scale a font to the screen size
     * @param f: the font to be scaled
     * @return the scaled font
     */
    public Font scaleFont(Font f) {
        return f.deriveFont(scaleW * f.getSize());
    }

    /**
     * Upscale a x coordinate to the original design value
     * @param x: the x value to be upscaled
     * @return upscaled value
     */
    public int upscaleX(double x) {
        return (int) Math.round(x * 1 / scaleW);
    }

    /**
     * Upscale a y coordinate to the original design value
     * @param y: the y value to be upscaled
     * @return upscaled value
     */
    public int upscaleY(double y) {
        return (int) Math.round(y * 1 / scaleH);
    }

    /**
     * Upscale a point to the original design value
     * @param p: the point to be upscaled
     * @return the upscaled point
     */
    public Point upscalePoint(Point p) {
        return new Point(upscaleX(p.getX()), upscaleY(p.getY()));
    }

    /**
     * Upscale a rectangle shape to the original design values
     * @param r: the rect to be upscaled
     * @return upscaled rect
     */
    public Rectangle upscaleRect(Rectangle r) {
        return new Rectangle(upscaleX(r.getX()), upscaleY(r.getY()), upscaleX(r.getWidth()), upscaleY(r.getHeight()));
    }

    /**
     * upscale any kind of shape to the original design value
     * @param s: the shape to be upscaled
     * @return upscaled shape
     */
    public Shape upscaleShape(Shape s) {
        return upscaleAf.createTransformedShape(s);
    }

    public void update() {
        scaleW = Game.config().renderConfiguration().getScaleWidth();
        scaleH = Game.config().renderConfiguration().getScaleHeight();

        af  = new AffineTransform();
        af.scale(scaleW, scaleH);

        upscaleAf = new AffineTransform();
        upscaleAf.scale(1/scaleW, 1/scaleH);
    }
}
