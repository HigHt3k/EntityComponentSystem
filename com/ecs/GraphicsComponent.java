package com.ecs;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GraphicsComponent extends Component {
    Rectangle bounds;
    Shape shape;
    BufferedImage image;
    String text;
    Font font;
    Color fillColor;
    Color textColor;
    Color borderColor;

    @Override
    public void update() {

    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}
