package com.ecs.component;

import com.Game;
import com.graphics.elements.ToolTip;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class GraphicsComponent extends Component {
    Rectangle bounds;
    Shape shape;
    BufferedImage image;
    Font font;
    Color fillColor;
    Color textColor;
    Color borderColor;
    boolean hovered = false;
    Color hoverColor;

    ArrayList<String> texts = new ArrayList<>();
    ArrayList<Point2D> locations = new ArrayList<>();

    ToolTip toolTip;

    @Override
    public void update() {

    }

    public void setToolTip(ToolTip toolTip) {
        this.toolTip = toolTip;
    }

    public ToolTip getToolTip() {
        return toolTip;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = Game.scale().scaleRect(bounds);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public ArrayList<String> getTexts() {
        return texts;
    }

    public ArrayList<Point2D> getLocations() {
        return locations;
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
        this.shape = Game.scale().scaleShape(shape);
    }

    public void addText(String text) {
        texts.add(text);
    }

    public void addLocation(Point p) {
        locations.add(Game.scale().scalePoint(p));
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
        this.font = Game.scale().scaleFont(font);
    }

    public Font getFont() {
        return font;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void hovered() {
        hovered = true;
    }

    public void unhovered() {
        hovered = false;
    }

    public boolean isHovered() {
        return hovered;
    }

    public boolean contains(Point2D p) {
        if(bounds.contains(p)) {
            return true;
        }
        return false;
    }

    public void reposition(Point p) {
        if(this.shape != null) {
            this.shape.getBounds().x = Game.scale().scaleX(p.x);
            this.shape.getBounds().y = Game.scale().scaleY(p.y);
        }
        if(this.bounds != null) {
            this.bounds.x = Game.scale().scaleX(p.x);
            this.bounds.y = Game.scale().scaleY(p.y);
        }
    }
}
