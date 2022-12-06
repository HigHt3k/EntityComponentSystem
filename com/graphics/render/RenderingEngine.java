package com.graphics.render;

import com.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * The Rendering Engine is the backbone of the graphics generation. It contains different methods which access sub-rendering engines
 * implemented for a special purpose each:
 * TextRenderer: renders Strings with formatting
 * ShapeRenderer: renders different shapes with given color and size
 * ImageRenderer: renders a buffered image at a position and size
 */
public class RenderingEngine {
    private static final float scaleW = Game.config().renderConfiguration().getScaleWidth();
    private static final float scaleH = Game.config().renderConfiguration().getScaleHeight();
    public static final int MARGIN = (int) (75 * scaleW); //px

    //TODO: handle alignment of texts, handle vertical size (height), auto scale if text doesn't fit box, handle
    // character amount per line in a smarter way -> this works, but not optimally
    /**
     * Render a String to a graphics instance.
     * @param g: graphics context
     * @param text: Text to render to screen
     * @param color: Text-color
     * @param x: position x
     * @param y: position y
     */
    public static void renderText(Graphics2D g, String text, Color color, int x, int y, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                Game.config().renderConfiguration().getAliasingText());
        width = Math.round(scaleW * width);
        height = Math.round(scaleH * height);
        x = Math.round(scaleW * x);
        y = Math.round(scaleH * y);

        int textWidth = g.getFontMetrics().stringWidth(text);
        int widthCharacter = g.getFontMetrics().stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789") / 36;
        int maxWidth = width - 2 * MARGIN;
        // detect the longest word in string and set the character per line size to maximum minus longest word.
        int charactersPerLine = maxWidth / widthCharacter - Objects.requireNonNull(Arrays.stream(text.split(" "))
                .max(Comparator.comparingInt(String::length))
                .orElse(null)).length();

        String regex = "(\\w{1,}.{1," + charactersPerLine + "})(\\s+|\\.)";
        if (textWidth > maxWidth) {
            text = text.replaceAll(regex, "$1\n");
        }
        TextRenderer.render(g, text, color, new Point((x + MARGIN), y));
    }

    public static void renderText(Graphics2D g, String text, Color color, Font font, int x, int y, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                Game.config().renderConfiguration().getAliasingText());
        width = Math.round(scaleW * width);
        height = Math.round(scaleH * height);
        x = Math.round(scaleW * x);
        y = Math.round(scaleH * y);

        int textWidth = g.getFontMetrics().stringWidth(text);
        int widthCharacter = g.getFontMetrics().stringWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789") / 36;
        int maxWidth = width - 2 * MARGIN;
        // detect the longest word in string and set the character per line size to maximum minus longest word.
        int charactersPerLine = maxWidth / widthCharacter - Objects.requireNonNull(Arrays.stream(text.split(" "))
                .max(Comparator.comparingInt(String::length))
                .orElse(null)).length();

        String regex = "(\\w{1,}.{1," + charactersPerLine + "})(\\s+|\\.)";
        if (textWidth > maxWidth) {
            text = text.replaceAll(regex, "$1\n");
        }
        TextRenderer.render(g, text, color, font, new Point(x + MARGIN,y));
    }

    /**
     * Render a shape (e.g. Rectangle, Ellipse2D, ...) to the graphics context. Can also be filled.
     * @param g: graphics context
     * @param shape Shape to render, e.g. Rectangle or Ellipse2D
     * @param color: Color to draw and fill
     * @param fill: fill or draw the shape
     */
    public static void renderShape(Graphics2D g, Shape shape, Color color, boolean fill) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        AffineTransform af = new AffineTransform();
        af.scale(scaleW, scaleH);
        ShapeRenderer.render(g, af.createTransformedShape(shape), color, fill);
    }

    /**
     * Render a Buffered Image to the screen at a given position and size in the graphics context.
     * @param g: Graphics2D context
     * @param img: Image to render
     * @param x: position x
     * @param y: position y
     * @param width: size in x direction (px)
     * @param height: size in y direction (px)
     */
    public static void renderImage(Graphics2D g, BufferedImage img, int x, int y, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                Game.config().renderConfiguration().getAntialiasing());

        x = Math.round(scaleW * x);
        y = Math.round(scaleH * y);
        width = Math.round(scaleW * width);
        height = Math.round(scaleH * height);
        ImageRenderer.render(g, img, x, y, width, height);
    }

    public void collectAndRenderEntities() {

    }
}
