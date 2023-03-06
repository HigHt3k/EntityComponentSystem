package engine.ecs.component.graphics.objects;

import engine.graphics.render.TextHorizontalAlignment;
import engine.graphics.render.TextVerticalAlignment;

import java.awt.*;

public class TextObject extends RenderObject {
    private String text;
    private final Font font;
    private Color color;
    private TextHorizontalAlignment horizontalAlignment;
    private TextVerticalAlignment verticalAlignment;

    public TextObject(Point location, Shape bounds, Layer layer, String text, Font font, Color color) {
        super(location, bounds, layer);
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public TextObject(Point location, Shape bounds, Layer layer, String text, Font font, Color color,
                      TextHorizontalAlignment horizontalAlignment, TextVerticalAlignment verticalAlignment) {
        super(location, bounds, layer);
        this.text = text;
        this.font = font;
        this.color = color;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public TextHorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public TextVerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }
}
