package com.ecs.component.graphics;

import com.Game;
import com.ecs.component.Component;
import com.ecs.component.graphics.GraphicsObject;
import com.ecs.entity.Entity;
import com.graphics.elements.ToolTip;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Stores all graphical information such as shapes, colors, text, lines, fonts, ...
 * Add this component to any {@link Entity} to render it to the screen properly.
 * Scaling is handled by the {@link com.graphics.render.ScalingEngine} that is applied to the graphics component upon
 * updating the size of any scalable context
 */
public class GraphicsComponent extends Component {
    // Refactoring the GraphicsComponent while still having possibility to use all the old stuff -> TODO: Remove later
    // What data does a graphics component contain?
    // - GraphicObject
    // - ToolTip
    ArrayList<GraphicsObject> graphicsObjects = new ArrayList<>();


    // original input sizes, needed when rescaling is done or entity is copied
    Rectangle _BOUNDS;
    Shape _SHAPE;
    Font _FONT;
    ArrayList<Point2D> _LOCATIONS = new ArrayList<>();
    int _THICKNESS = 0;
    ArrayList<Shape> _SHAPES = new ArrayList<>();

    // temporary sizes
    Rectangle bounds;
    Shape shape;
    Font font;
    ArrayList<Point2D> locations;
    Point lineStart;
    Point lineEnd;
    int thickness;
    ArrayList<Shape> shapes = new ArrayList<>();

    // non scalable content
    BufferedImage image;
    Color fillColor;
    Color textColor;
    Color borderColor;
    Color lineColor;
    boolean hovered = false;
    Color hoverColor;

    ArrayList<String> texts = new ArrayList<>();

    ToolTip toolTip;

    @Override
    public void update() {

    }

    public void addGraphicsObject(GraphicsObject graphicsObject) {
        graphicsObjects.add(graphicsObject);
    }

    public ArrayList<GraphicsObject> getGraphicsObjects() {
        return graphicsObjects;
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

        if(!_SHAPES.isEmpty()) {
            shapes = new ArrayList<>();
            for (int i = 0; i < _SHAPES.size(); i++) {
                shapes.add(Game.scale().scaleShape(_SHAPES.get(i)));
            }
        }

        if(_THICKNESS != 0) {
            thickness = Game.scale().scaleX(_THICKNESS);
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
        if(this.shape != null) {
            this.shape.getBounds().x = p.x;
            this.shape.getBounds().y = p.y;

            this._SHAPE.getBounds().x = Game.scale().upscaleX(p.x);
            this._SHAPE.getBounds().y = Game.scale().upscaleY(p.y);
        }

        if(this.bounds != null) {
            this.bounds.x = p.x;
            this.bounds.y = p.y;

            this._BOUNDS.x = Game.scale().upscaleX(p.x);
            this._BOUNDS.y = Game.scale().upscaleY(p.y);
        }
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

    public Point getLineStart() {
        return lineStart;
    }

    public Point getLineEnd() {
        return lineEnd;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color color) {
        this.lineColor = color;
    }

    public void setLine(Point p1, Point p2) {
        this.lineStart = p1;
        this.lineEnd = p2;
    }

    public void addShape(Shape s) {
        _SHAPES.add(s);
        updateSize();
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }
}
