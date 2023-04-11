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
 * the main class for the game, contains the engine and core pieces of the implementation
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

    public static ResourceManager res() {
        return res;
    }

    public static SoundEngine sound() {
        return sound;
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

    public static InputManager input() {
        return inputManager;
    }

    public static SystemManager system() {
        return systemManager;
    }

    public static RenderingEngine graphics() {return graphics;}

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
