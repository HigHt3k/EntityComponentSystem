package game;

import engine.Game;
import engine.ecs.system.ActionSystem;
import engine.ecs.system.CollisionDetectionSystem;
import engine.resource.lang.LanguageType;
import game.handler.builder.BuildHandler;
import game.handler.CursorSelectorHandler;
import game.handler.SimulationSystem;
import game.scenes.BuildScene;
import game.scenes.LevelScene;
import game.scenes.MenuScene;
import game.scenes.test.TestScene;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
        Game.info().setTitle("Entity Component System Test");
        Game.info().setAuthor("Johann Töpfer");
        Game.info().setVersion("v0.0.1-SNAPSHOT");
        Game.info().setDescription("Educational game to show the concept of redundancy in aircraft system engineering.");
        Game.config().setLanguage(LanguageType.EN_US);
        Game.config().setDebug(true);
        Game.init();

        Game.logger().setLevel(Level.ALL);
        Game.frame().setIcon("res/avionics/crdc.png");
        Game.res().loadTileSet("res/base_tiles.xml");
        Game.res().language().parseLanguageFile("res/lang/de_de.xml");
        Game.res().language().parseLanguageFile("res/lang/de_simple.xml");
        Game.res().language().parseLanguageFile("res/lang/en_us.xml");

        File folder = new File("res/level/tutorial");
        File[] listOfFiles = folder.listFiles();

        try {
            for (File f : Objects.requireNonNull(listOfFiles)) {
                Game.res().loadLevel(f.getPath());
            }
        } catch (NullPointerException ex) {
            Game.logger().severe("No levels found, please add levels to the res/level folder \n" + ex);
            // Exit game with a warning on screen
        }

        folder = new File("res/level/easy");
        listOfFiles = folder.listFiles();

        try {
            for (File f : Objects.requireNonNull(listOfFiles)) {
                Game.res().loadLevel(f.getPath());
            }
        } catch(NullPointerException ex) {
            Game.logger().severe("No levels found, please add levels to the res/level folder \n" + ex);
            // Exit game with a warning on screen
        }

        folder = new File("res/level/medium");
        listOfFiles = folder.listFiles();

        try {
            for (File f : Objects.requireNonNull(listOfFiles)) {
                Game.res().loadLevel(f.getPath());
            }
        } catch(NullPointerException ex) {
            Game.logger().severe("No levels found, please add levels to the res/level folder \n" + ex);
            // Exit game with a warning on screen
        }

        folder = new File("res/level/hard");
        listOfFiles = folder.listFiles();

        try {
            for (File f : Objects.requireNonNull(listOfFiles)) {
                Game.res().loadLevel(f.getPath());
            }
        } catch (NullPointerException ex) {
            Game.logger().severe("No levels found, please add levels to the res/level folder \n" + ex);
            // Exit game with a warning on screen
        }

        //Game.scene().addScene(new TestTextScene("Test", -260));
        //Game.scene().setCurrentScene(-260);
        Game.scene().addScene(new MenuScene("Menu", -255));
        Game.scene().addScene(new TestScene("test", -1000));
        Game.scene().addScene(new LevelScene("level", -254));
        Game.scene().addScene(new BuildScene("build", -250));
        Game.scene().setCurrentScene(-255);
        Game.scene().initScenes();
        Game.system().addSystem(new SimulationSystem());
        Game.system().addSystem(new ActionSystem());
        Game.input().addHandler(new CollisionDetectionSystem());
        Game.input().addHandler(new CursorSelectorHandler());
        Game.input().addHandler(new BuildHandler());

        //Game.scene().addScene(new GraphicObjectsTestScene("TEST", -1000));
        //Game.scene().setCurrentScene(-1000);

        Game.start();
    }
}
