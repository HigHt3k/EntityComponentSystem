package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

import java.awt.*;

/**
 * Class containing a collection of fonts used in the application.
 */
public class FontCollection {
    public static Font bit8Font = Game.res().loadFont("res/font/joystix monospace.ttf", 18f);
    public static Font bit8FontLarge = FontCollection.scaleFont(bit8Font, 20f);
    public static Font bit8FontHuge = FontCollection.scaleFont(bit8Font, 35f);
    public static Font bit8FontMedium = FontCollection.scaleFont(bit8Font, 16f);
    public static Font bit8FontSmall = FontCollection.scaleFont(bit8Font, 12f);

    /**
     * Scales the provided font to the specified size.
     *
     * @param f The font to be scaled.
     * @param size The desired font size.
     * @return A new Font object with the specified size.
     */
    public static Font scaleFont(Font f, float size) {
        return f.deriveFont(size);
    }
}
