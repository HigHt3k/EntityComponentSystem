package com.resource;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Logger;

public class TileSet {
    private final HashMap<Integer, BufferedImage> tiles;

    public TileSet() {
        this.tiles = new HashMap<>();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Tileset loaded.");
    }

    public HashMap<Integer, BufferedImage> getTiles() {
        return tiles;
    }

    public BufferedImage getTile(int id) {
        return tiles.get(id);
    }

    public BufferedImage getTile(String id) {
        return tiles.get(Integer.parseInt(id));
    }

}
