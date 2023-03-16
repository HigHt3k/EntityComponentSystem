package engine.graphics.elements;

import engine.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle(Game.info().getTitle());
        Game.logger().info(Game.info().getTitle());

        this.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                resize();
            }
        });

        this.renderPanel.init();
        this.getContentPane().setBackground(new Color(0, 0, 0));
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
            Game.frame().setResizable(false);
        } else {
            Game.frame().dispose();
            Game.frame().setExtendedState(JFrame.NORMAL);
            Game.frame().setUndecorated(false);
            Game.frame().setVisible(true);
            Game.frame().setResizable(true);
        }
    }

    /**
     * Resize the GameFrame
     */
    private void resize() {
        this.renderPanel.setNewSize();
        Game.scale().update();
    }
}
