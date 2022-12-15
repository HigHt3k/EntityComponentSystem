package com;

import com.config.GameConfiguration;
import com.graphics.SceneManager;
import com.graphics.elements.GameFrame;
import com.graphics.elements.RenderPanel;
import com.graphics.render.RenderingEngine;
import com.graphics.scene.Scene;
import com.input.InputManager;
import com.logic.GameLogic;
import com.resource.ResourceManager;

import java.util.logging.Logger;

/**
 * the main class for the game, contains the engine and core pieces of the implementation
 */
public class Game {
    // Logger & Config
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final GameConfiguration config = new GameConfiguration();
    private static final GameInformation info = new GameInformation();

    // Managers
    private static final RenderingEngine graphics = new RenderingEngine();
    private static final ResourceManager res = new ResourceManager();
    private static SceneManager sceneManager;
    private static InputManager inputManager;


    // Display stuff
    private static GameFrame gameFrame;

    // Threads
    private static GameLoop gameLoop;

    // Logic
    private static GameLogic gameLogic;

    // variables
    private static boolean paused = false;

    public static ResourceManager res() {
        return res;
    }

    public static GameConfiguration config() {
        return config;
    }

    public static GameInformation info() {
        return info;
    }

    public static SceneManager scene() {
        return sceneManager;
    }

    public static Logger logger() {
        return logger;
    }

    public static GameFrame frame() {
        return gameFrame;
    }

    public static GameLogic logic() {
        return gameLogic;
    }

    public static InputManager input() {
        return inputManager;
    }

    public static RenderingEngine graphics() {return graphics;}

    /**
     * initialize all instances needed for the game that need to be
     * initialized in a certain in order due to class interconnection
     */
    public static synchronized void init() {
        inputManager = new InputManager() {
            @Override
            public void handle() {

            }
        };
        gameFrame = new GameFrame();
        sceneManager = new SceneManager();
        gameLogic = new GameLogic();
        gameLoop = new GameLoop();
        logger.info("Game initialized successfully");

    }

    /**
     * start the game
     */
    public static synchronized void start() {
        logger.info("Game started");
        gameLoop.setStarted();
        gameLoop.start();
    }

    public static void setInputManager(InputManager i) {
        inputManager = i;
    }

    public static void unpause() {
        gameLoop.unpause();
        paused = false;
    }

    public static void pause() {
        gameLoop.pause();
        paused = true;
    }

    public static boolean paused() {
        return paused;
    }

}
