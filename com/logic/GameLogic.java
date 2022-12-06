package com.logic;

import com.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the games logic for all implemented logics of the game.
 * extend this class to implement the games update logic
 */
public class GameLogic {
    private final List<GameLogic> logics = new ArrayList<>();

    /**
     * update all elements of the logics list
     */
    public void update() {
        for (GameLogic l : logics) {
            l.update();
        }
    }

    /**
     * add a new game logic to the game. customized
     * @param logic
     */
    public void addLogic(GameLogic logic) {
        logics.add(logic);
        Game.logger().info("Added new GameLogic: " + logic);
        Game.logger().info("GameLogic stack now contains " + logics.size() + " instances to update.");
    }

}
