package engine.graphics.elements;

import engine.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame {
    private final RenderPanel renderPanel;

    public GameFrame() {
        Dimension resolution = Game.config().renderConfiguration().getResolution();
        this.renderPanel = new RenderPanel(resolution);
        this.add(renderPanel);

        this.setSize(resolution);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        System.out.println(Game.info().getTitle());
        this.setTitle(Game.info().getTitle());

        this.renderPanel.init();
    }

    public RenderPanel getRenderPanel() {
        return renderPanel;
    }

    public void setIcon(String path) {
        try {
            setIconImage(ImageIO.read(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toggleFullScreen() {
        if(Game.config().renderConfiguration().getFullscreenMode()) {
            Game.frame().dispose();
            Game.frame().setExtendedState(JFrame.MAXIMIZED_BOTH);
            Game.frame().setUndecorated(true);
            Game.frame().setVisible(true);
        } else {
            Game.frame().dispose();
            Game.frame().setExtendedState(JFrame.NORMAL);
            Game.frame().setUndecorated(false);
            Game.frame().setVisible(true);
        }
    }
}
