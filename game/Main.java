package game;

import com.Game;
import game.scenes.GameScene;
import game.scenes.MenuScene;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
        Game.info().setTitle("Entity Component System Test");
        Game.info().setAuthor("Johann Töpfer");
        Game.info().setVersion("v0.0.1-SNAPSHOT");
        Game.info().setDescription("Educational game to show the concept of redundancy in aircraft system engineering.");
        Game.config().setDebug(true);
        Game.init();

        Game.logger().setLevel(Level.ALL);
        Game.frame().setIcon("game/res/avionics/crdc.png");
        Game.res().loadTileSet("game/res/base_tiles.xml");

        File folder = new File("game/res/level");
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

        Game.start();
    }
}
