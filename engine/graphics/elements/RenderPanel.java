package engine.graphics.elements;

import engine.Game;
import engine.graphics.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RenderPanel extends JPanel {

    public RenderPanel(final Dimension size) {
        this.setSize(size);
        this.setBackground(new Color(80, 80, 80));
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

    public void init() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Game.input().queueEvent(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
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
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                Game.input().queueEvent(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                Game.input().queueEvent(e);
            }
        });
    }

}
