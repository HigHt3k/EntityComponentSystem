package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.tiles;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TileSet {
    private final HashMap<Integer, BufferedImage> tiles;
    private final HashMap<Integer, String> descriptions;
    private final HashMap<Integer, SimulationType> types;
    private final HashMap<Integer, Integer> minNonPassives;

    public HashMap<Integer, Integer> getMinNonPassives() {
        return minNonPassives;
    }

    public TileSet() {
        this.tiles = new HashMap<>();
        this.descriptions = new HashMap<>();
        this.types = new HashMap<>();
        this.minNonPassives = new HashMap<>();
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
