package com.ecs.component.object;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class GraphicsObject {
    // Data
    Shape shape;
    BufferedImage image;
    String text;
    Font font;
    Color color;
    Color borderColor;
    Line2D line;
    final Color hoverColor = new Color(200, 200, 200, 60);
    GraphicsObjectType type;
    // rendering engine will sort objects by layer
    int layer = 0;


    /**
     * text object
     * @param text: text to render
     * @param font: font to use
     * @param color: color of text
     * @param shape: boundary of text
     */
    public GraphicsObject(String text, Font font, Color color, Shape shape) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.shape = shape;
        this.type = GraphicsObjectType.TEXT;
    }

    /**
     * create new image object
     * @param image
     * @param shape
     */
    public GraphicsObject(BufferedImage image, Shape shape) {
        this.image = image;
        this.shape = shape;
        this.type = GraphicsObjectType.IMAGE;
    }

    /**
     * Shape object, if either color or borderColor is null, the object will not have a fill / border color
     * @param shape: The shape to be rendered
     * @param color: the fill color of the shape
     * @param borderColor: the border color of the shape
     */
    public GraphicsObject(Shape shape, Color color, Color borderColor) {
        this.shape = shape;
        this.color = color;
        this.borderColor = borderColor;
        this.type = GraphicsObjectType.SHAPE;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Color getColor() {
        return color;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public Font getFont() {
        return font;
    }

    public Line2D getLine() {
        return line;
    }

    public Shape getShape() {
        return shape;
    }

    public String getText() {
        return text;
    }

    public GraphicsObjectType getType() {
        return type;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
