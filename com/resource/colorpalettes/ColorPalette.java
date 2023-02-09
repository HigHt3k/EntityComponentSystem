package com.resource.colorpalettes;

import java.awt.*;

public class ColorPalette {

    public static Color setAlpha(Color c, int a) {
        return new Color(c.getRGBColorComponents(null)[0],
                c.getRGBColorComponents(null)[1],
                c.getRGBColorComponents(null)[2],
                a/255F);
    }
}
