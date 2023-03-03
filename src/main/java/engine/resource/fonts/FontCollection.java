package engine.resource.fonts;

import engine.Game;

import java.awt.*;

public class FontCollection {
    public static Font bit8Font = Game.res().loadFont("res/font/joystix monospace.ttf", 18f);
    public static Font bit8FontLarge = FontCollection.scaleFont(bit8Font, 20f);
    public static Font bit8FontHuge = FontCollection.scaleFont(bit8Font, 35f);
    public static Font bit8FontMedium = FontCollection.scaleFont(bit8Font, 16f);
    public static Font bit8FontSmall = FontCollection.scaleFont(bit8Font, 12f);

    public static Font scaleFont(Font f, float size) {
        return f.deriveFont(size);
    }
}
