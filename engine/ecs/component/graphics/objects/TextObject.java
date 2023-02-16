package engine.ecs.component.graphics.objects;

import java.awt.*;

public class TextObject extends RenderObject {
    private String text;
    private final Font font;
    private final Color color;

    public TextObject(Point location, Shape bounds, Layer layer, String text, Font font, Color color) {
        super(location, bounds, layer);
        this.text = text;
        this.font = font;
        this.color = color;
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
}
