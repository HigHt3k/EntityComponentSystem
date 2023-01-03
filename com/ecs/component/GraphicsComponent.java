package com.ecs.component;

import com.Game;
import com.graphics.elements.ToolTip;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class GraphicsComponent extends Component {
    // original input sizes, needed when rescaling is done or entity is copied
    Rectangle _BOUNDS;
    Shape _SHAPE;
    Font _FONT;
    ArrayList<Point2D> _LOCATIONS = new ArrayList<>();
    Point _LINESTART;
    Point _LINEEND;
    int _THICKNESS = 0;

    // temporary sizes
    Rectangle bounds;
    Shape shape;
    Font font;
    ArrayList<Point2D> locations;
    Point lineStart;
    Point lineEnd;
    int thickness;

    // non scalable content
    BufferedImage image;
    Color fillColor;
    Color textColor;
    Color borderColor;
    boolean hovered = false;
    Color hoverColor;

    ArrayList<String> texts = new ArrayList<>();

    ToolTip toolTip;

    @Override
    public void update() {

    }

    public void updateSize() {
        if(_BOUNDS != null)
            this.bounds = Game.scale().scaleRect(_BOUNDS);

        if(_SHAPE != null)
            this.shape = Game.scale().scaleShape(_SHAPE);

        if(_FONT != null)
            this.font = Game.scale().scaleFont(_FONT);

        if(!_LOCATIONS.isEmpty()) {
            locations = new ArrayList<>();
            for (int i = 0; i < _LOCATIONS.size(); i++) {
                locations.add(Game.scale().scalePoint((Point) _LOCATIONS.get(i)));
            }
        }

        if(_THICKNESS != 0) {
            thickness = Game.scale().scaleX(_THICKNESS);
        }

        if(_LINESTART != null && _LINEEND != null) {
            lineStart = Game.scale().scalePoint(_LINESTART);
            lineEnd = Game.scale().scalePoint(_LINEEND);
        }
    }

    public void setThickness(int thickness) {
        this._THICKNESS = thickness;
        updateSize();
    }

    public int getThickness() {
        return thickness;
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
        this._BOUNDS = bounds;
        updateSize();
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
        this._SHAPE = shape;
        updateSize();
    }

    public void addText(String text) {
        texts.add(text);
    }

    public void addLocation(Point p) {
        _LOCATIONS.add(p);
        updateSize();
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
        this._FONT = font;
        updateSize();
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
        if(this._SHAPE != null) {
            this._SHAPE.getBounds().x = p.x;
            this._SHAPE.getBounds().y = p.y;
        }

        if(this._BOUNDS != null) {
            this._BOUNDS.x = p.x;
            this._BOUNDS.y = p.y;
        }

        updateSize();
    }

    public Rectangle get_BOUNDS() {
        return _BOUNDS;
    }

    public Shape get_SHAPE() {
        return _SHAPE;
    }

    public Font get_FONT() {
        return _FONT;
    }

    public ArrayList<Point2D> get_LOCATIONS() {
        return _LOCATIONS;
    }

    public Point get_LINESTART() {
        return _LINESTART;
    }

    public Point get_LINEEND() {
        return _LINEEND;
    }

    public Point getLineStart() {
        return lineStart;
    }

    public Point getLineEnd() {
        return lineEnd;
    }

    public void setLine(Point p1, Point p2) {
        this._LINESTART = p1;
        this._LINEEND = p2;
        updateSize();
    }
}
