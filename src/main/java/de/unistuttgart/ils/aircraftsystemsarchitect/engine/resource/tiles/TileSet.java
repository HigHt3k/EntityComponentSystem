package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.tiles;

import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * The TileSet class represents a set of tiles used for rendering the game map.
 * It stores the images for each tile, as well as their descriptions, simulation types,
 * and minimum number of non-passive tiles required to render the tile.
 */
public class TileSet {

    /**
     * The HashMap of tile images, where the key is the tile ID.
     */
    private final HashMap<Integer, BufferedImage> tiles;

    /**
     * The HashMap of tile descriptions, where the key is the tile ID.
     */
    private final HashMap<Integer, String> descriptions;

    /**
     * The HashMap of simulation types for each tile, where the key is the tile ID.
     */
    private final HashMap<Integer, SimulationType> types;

    /**
     * The HashMap of minimum number of non-passive tiles required to render each tile, where the key is the tile ID.
     */
    private final HashMap<Integer, Integer> minNonPassives;

    /**
     * Constructs a new TileSet object.
     */
    public TileSet() {
        this.tiles = new HashMap<>();
        this.descriptions = new HashMap<>();
        this.types = new HashMap<>();
        this.minNonPassives = new HashMap<>();
    }

    /**
     * Retrieves the HashMap of tile images.
     *
     * @return The HashMap of tile images.
     */
    public HashMap<Integer, BufferedImage> getTiles() {
        return tiles;
    }

    /**
     * Retrieves the tile image for the specified tile ID.
     *
     * @param id The ID of the tile for which to retrieve the image.
     * @return The tile image for the specified tile ID.
     */
    public BufferedImage getTile(int id) {
        return tiles.get(id);
    }

    /**
     * Retrieves the tile image for the specified tile ID as a String.
     *
     * @param id The ID of the tile for which to retrieve the image.
     * @return The tile image for the specified tile ID as a String.
     */
    public BufferedImage getTile(String id) {
        return tiles.get(Integer.parseInt(id));
    }

    /**
     * Retrieves the description for the specified tile ID.
     *
     * @param id The ID of the tile for which to retrieve the description.
     * @return The description for the specified tile ID.
     */
    public String getDescription(int id) {
        return descriptions.get(id);
    }

    /**
     * Retrieves the HashMap of tile descriptions.
     *
     * @return The HashMap of tile descriptions.
     */
    public HashMap<Integer, String> getDescriptions() {
        return descriptions;
    }

    /**
     * Retrieves the HashMap of simulation types for each tile.
     *
     * @return The HashMap of simulation types for each tile.
     */
    public HashMap<Integer, SimulationType> getTypes() {
        return types;
    }

    /**
     * Retrieves the simulation type for the specified tile ID.
     *
     * @param id The ID of the tile for which to retrieve the simulation type.
     * @return The simulation type for the specified tile ID.
     */
    public SimulationType getType(int id) {
        return types.get(id);
    }

    /**
     * Retrieves the HashMap of minimum number of non-passive tiles required to render each tile.
     *
     * @return The HashMap of minimum number of non-passive tiles required to render each tile.
     */
    public HashMap<Integer, Integer> getMinNonPassives() {
        return minNonPassives;
    }
}
