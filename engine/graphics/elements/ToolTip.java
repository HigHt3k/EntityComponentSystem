package engine.graphics.elements;

import java.awt.*;

public class ToolTip {
    private Color toolTipColor = new Color(240, 240, 150, 255);
    private Color borderColor = new Color(80, 80, 80, 255);
    private Color textColor = new Color(60, 60, 60, 255);
    private Font font;
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getToolTipColor() {
        return toolTipColor;
    }

    public Color getTextColor() {
        return textColor;
    }
}
