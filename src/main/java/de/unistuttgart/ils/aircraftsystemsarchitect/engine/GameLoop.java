package de.unistuttgart.ils.aircraftsystemsarchitect.engine;

import java.awt.event.MouseEvent;

/**
 * Standard Game Thread that updates all components of the Game at the given tickrate
 */
public class GameLoop extends Thread {
    private final int DEFAULT_TICK_RATE = Game.config().getDefaultTickRate();
    private boolean paused = true;
    private boolean started = false;

    /**
     * runs the standard game thread at a set tick rate / update rate
     */
    @Override
    public void run() {
        while(!interrupted()) {
            this.process();
            try {
                sleep(DEFAULT_TICK_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * processes all the updates that need to be done at each tick
     */
    private void process() {
        if(started) {
            // handle inputs
            Game.input().handle();
            // handle systems
            Game.system().handle();
            // render entities
            Game.frame().getRenderPanel().repaint();
            // play sounds
            Game.sound().collectAndPlayEntities();
        }
    }

    protected void pause() {
        paused = true;
    }

    protected void unpause() {
        paused = false;
    }

    public void setStarted() {started = true; }
}
