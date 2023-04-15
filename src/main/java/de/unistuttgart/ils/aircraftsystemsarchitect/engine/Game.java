package de.unistuttgart.ils.aircraftsystemsarchitect.engine;

import com.badlogic.gdx.files.FileHandle;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.config.GameConfiguration;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system.SystemManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.SceneManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.elements.GameFrame;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render.RenderingEngine;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render.ScalingEngine;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.InputManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.ResourceManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.sound.SoundEngine;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.CursorSelectorHandler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The main class for the game, containing the engine and core pieces of the implementation.
 */
public class Game {

    // Logger & Config
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final GameConfiguration config = new GameConfiguration();
    private static final GameInformation info = new GameInformation();

    // Managers
    private static final RenderingEngine graphics = new RenderingEngine();
    private static final ScalingEngine scale = new ScalingEngine();
    private static final ResourceManager res = new ResourceManager();
    private static final SoundEngine sound = new SoundEngine();
    private static SceneManager sceneManager;
    private static InputManager inputManager;
    private static SystemManager systemManager;

    // Display stuff
    private static GameFrame gameFrame;

    // Threads
    private static GameLoop gameLoop;

    // variables
    private static boolean paused = false;

    /**
     * Get the ResourceManager instance.
     * @return The ResourceManager instance.
     */
    public static ResourceManager res() {
        return res;
    }

    /**
     * Get the SoundEngine instance.
     * @return The SoundEngine instance.
     */
    public static SoundEngine sound() {
        return sound;
    }

    /**
     * Get the GameConfiguration instance.
     * @return The GameConfiguration instance.
     */
    public static GameConfiguration config() {
        return config;
    }

    /**
     * Get the GameInformation instance.
     * @return The GameInformation instance.
     */
    public static GameInformation info() {
        return info;
    }

    /**
     * Get the SceneManager instance.
     * @return The SceneManager instance.
     */
    public static SceneManager scene() {
        return sceneManager;
    }

    /**
     * Get the Logger instance.
     * @return The Logger instance.
     */
    public static Logger logger() {
        return logger;
    }

    /**
     * Get the GameFrame instance.
     * @return The GameFrame instance.
     */
    public static GameFrame frame() {
        return gameFrame;
    }

    /**
     * Get the InputManager instance.
     * @return The InputManager instance.
     */
    public static InputManager input() {
        return inputManager;
    }

    /**
     * Get the SystemManager instance.
     * @return The SystemManager instance.
     */
    public static SystemManager system() {
        return systemManager;
    }

    /**
     * Get the RenderingEngine instance.
     * @return The RenderingEngine instance.
     */
    public static RenderingEngine graphics() {
        return graphics;
    }

    /**
     * Get the ScalingEngine instance
     * @return The ScalingEngine instance
     */
    public static ScalingEngine scale() {return scale;}

    /**
     * initialize all instances needed for the game that need to be
     * initialized in a certain in order due to class interconnection
     */
    public static synchronized void init() {
        systemManager = new SystemManager();
        inputManager = new InputManager();
        gameFrame = new GameFrame();
        sceneManager = new SceneManager();
        gameLoop = new GameLoop();

        try {
            FileHandler f = new FileHandler("logs.log");
            logger.addHandler(f);
            SimpleFormatter formatter = new SimpleFormatter();
            f.setFormatter(formatter);

            logger.info(info.getTitle() + " by " + info.getAuthor() + "\nVersion: " + info.getVersion() + "\n" + info.getDescription());
            logger.info("Game initialized successfully.");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * start the game
     */
    public static synchronized void start() {
        input().getHandler(CursorSelectorHandler.class).init();
        logger.info("Game started");
        gameLoop.setStarted();
        gameLoop.start();
    }

    /**
     * unpause the game
     */
    public static void unpause() {
        gameLoop.unpause();
        paused = false;
    }

    /**
     * pause the game
     */
    public static void pause() {
        gameLoop.pause();
        paused = true;
    }

    /**
     * check if game is paused
     * @return true if paused, false else
     */
    public static boolean paused() {
        return paused;
    }

}
