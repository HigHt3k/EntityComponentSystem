package game;

import com.Game;
import com.resource.lang.LanguageType;
import game.scenes.MenuScene;
import game.scenes.test.GraphicObjectsTestScene;

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
        Game.frame().setIcon("game/res/avionics/crdc.png");
        Game.res().loadTileSet("game/res/base_tiles.xml");
        Game.res().language().parseLanguageFile("game/res/lang/de_de.xml");
        Game.res().language().parseLanguageFile("game/res/lang/de_simple.xml");
        Game.res().language().parseLanguageFile("game/res/lang/en_us.xml");

        File folder = new File("game/res/level/tutorial");
        File[] listOfFiles = folder.listFiles();

        try {
            for (File f : Objects.requireNonNull(listOfFiles)) {
                Game.res().loadLevel(f.getPath());
            }
        } catch(NullPointerException ex) {
            Game.logger().severe("No levels found, please add levels to the res/level folder \n" + ex);
            // Exit game with a warning on screen
        }

        Game.scene().addScene(new MenuScene("Menu", -255));
        Game.scene().setCurrentScene(-255);

        //Game.scene().addScene(new GraphicObjectsTestScene("TEST", -1000));
        //Game.scene().setCurrentScene(-1000);

        Game.start();
    }
}
