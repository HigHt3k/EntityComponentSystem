package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes;

import java.awt.*;

/**
 * Class containing utility methods for manipulating colors.
 */
public class ColorPalette {

    /**
     * Creates a new color with the specified alpha value from a given color.
     *
     * @param c The base color.
     * @param a The desired alpha value (0-255).
     * @return A new Color object with the specified alpha value.
     */
    public static Color setAlpha(Color c, int a) {
        return new Color(c.getRGBColorComponents(null)[0],
                c.getRGBColorComponents(null)[1],
                c.getRGBColorComponents(null)[2],
                a/255F);
    }
}
