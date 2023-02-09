package com.resource.fonts;

import com.Game;

import java.awt.*;

public class FontCollection {
    public static Font bit8Font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f);

    public static Font scaleFont(Font f, float size) {
        return f.deriveFont(size);
    }
}
