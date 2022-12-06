package game;

import com.Game;
import game.scenes.GameScene;
import game.scenes.MenuScene;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
        Game.info().setTitle("ENGINEer-Test");
        Game.init();

        Game.logger().setLevel(Level.ALL);
        Game.frame().setIcon("test/res/avionics/crdc.png");
        Game.res().loadTileSet("test/res/base_tiles.xml");

        File folder = new File("test/res/level");
        File[] listOfFiles = folder.listFiles();

        try {
            for (File f : Objects.requireNonNull(listOfFiles)) {
                Game.res().loadLevel(f.getPath());
            }
        } catch(NullPointerException ex) {
            Game.logger().severe("No levels found, please add levels to the res/level folder \n" + ex);
        }

        Game.scene().addScene(new MenuScene("Menu", 0));
        Game.scene().setCurrentScene(0);

        Game.start();
    }
}
