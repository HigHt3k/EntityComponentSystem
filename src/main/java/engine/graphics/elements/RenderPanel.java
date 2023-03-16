package engine.graphics.elements;

import engine.Game;
import engine.graphics.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RenderPanel extends JPanel {

    public RenderPanel(final Dimension size) {
        this.setSize(size);
        this.setBackground(new Color(0, 0, 0));
        this.setPreferredSize(size);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Game.graphics().setGraphics((Graphics2D) g);
        super.paintComponent(g);
        Game.graphics().collectAndRenderEntities();
    }

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

            @Override
            public synchronized void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
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

    public void setNewSize() {
        Game.config().renderConfiguration().resize();

        this.setSize(Game.config().renderConfiguration().getResolution());
        this.setPreferredSize(Game.config().renderConfiguration().getResolution());
    }

}
