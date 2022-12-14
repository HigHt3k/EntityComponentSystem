package com.resource;

import com.Game;
import game.components.SimulationType;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.logging.Logger;

public class TileSet {
    private final HashMap<Integer, BufferedImage> tiles;
    private final HashMap<Integer, String> descriptions;
    private final HashMap<Integer, SimulationType> types;

    public TileSet() {
        this.tiles = new HashMap<>();
        this.descriptions = new HashMap<>();
        this.types = new HashMap<>();
        Game.logger().info("Tileset loaded.");
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

    public String getDescription(int id) {
        return descriptions.get(id);
    }

    public HashMap<Integer, String> getDescriptions() {
        return descriptions;
    }

    public HashMap<Integer, SimulationType> getTypes() {
        return types;
    }

    public SimulationType getType(int id) {
        return types.get(id);
    }
}
