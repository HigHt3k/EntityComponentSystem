package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.elements;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

/**
 * Class representing the main game window for the aircraft systems architect engine.
 * This class is responsible for creating, updating, and managing the display of the game window,
 * including the RenderPanel, window resizing, and full-screen mode toggling.
 */
public class GameFrame extends JFrame {
    /**
     * The RenderPanel instance used for displaying game elements.
     */
    private final RenderPanel renderPanel;

    /**
     * Constructor for the GameFrame class.
     * Initializes and configures the game window, including the RenderPanel,
     * title, size, and component listener for resizing.
     */
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

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                resize();
            }
        });

        this.renderPanel.init();
        this.getContentPane().setBackground(new Color(0, 0, 0));
    }

    /**
     * Getter for the RenderPanel instance.
     *
     * @return The RenderPanel instance used by the GameFrame.
     */
    public RenderPanel getRenderPanel() {
        return renderPanel;
    }

    /**
     * Sets the icon of the game window using an image file at the specified path.
     *
     * @param path The file path of the image to be used as the icon.
     */
    public void setIcon(String path) {
        try {
            setIconImage(ImageIO.read(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Toggles the full-screen mode of the game window.
     * If the game is in full-screen mode, it will switch to windowed mode,
     * and vice versa.
     */
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
     * Resizes the GameFrame, updating the RenderPanel and game scaling.
     */
    private void resize() {
        this.renderPanel.setNewSize();
        Game.scale().update();
    }
}
