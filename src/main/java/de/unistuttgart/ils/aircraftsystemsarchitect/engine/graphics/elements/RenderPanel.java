package de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.elements;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class representing the render panel within the aircraft systems architect engine.
 * This class is responsible for rendering game elements, handling user input events,
 * and updating its size when the game window is resized.
 */
public class RenderPanel extends JPanel {

    /**
     * Constructor for the RenderPanel class.
     * Initializes the panel with the specified size and sets the background color to black.
     *
     * @param size The initial dimensions of the RenderPanel.
     */
    public RenderPanel(final Dimension size) {
        this.setSize(size);
        this.setBackground(new Color(0, 0, 0));
        this.setPreferredSize(size);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Paints the game elements on the RenderPanel.
     *
     * @param g The Graphics object to be used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Game.graphics().setGraphics((Graphics2D) g);
        super.paintComponent(g);
        Game.graphics().collectAndRenderEntities();
    }

    /**
     * Initializes the event listeners for handling user input events.
     */
    public synchronized void init() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public synchronized void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Game.input().queueEvent(e);
            }

            @Override
            public synchronized void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public synchronized void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        });

        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public synchronized void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                Game.input().queueEvent(e);
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Game.input().queueEvent(e);
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public synchronized void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public synchronized void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                Game.input().queueEvent(e);
            }

            @Override
            public synchronized void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                Game.input().queueEvent(e);
            }
        });
    }

    /**
     * Updates the size of the RenderPanel based on the game configuration.
     */
    public void setNewSize() {
        Game.config().renderConfiguration().resize();

        this.setSize(Game.config().renderConfiguration().getResolution());
        this.setPreferredSize(Game.config().renderConfiguration().getResolution());
    }

}
