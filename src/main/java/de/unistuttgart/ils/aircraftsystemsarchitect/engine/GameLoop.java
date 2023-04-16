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
     * Runs the standard game thread at a set tick rate / update rate.
     * Calls the process method at each tick.
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
     * Processes all the updates that need to be done at each tick.
     * Handles inputs, systems, entities rendering and sound playback.
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

    /**
     * Pauses the game loop, stopping updates and rendering.
     */
    protected void pause() {
        paused = true;
    }

    /**
     * Resumes the game loop, allowing updates and rendering to continue.
     */
    protected void unpause() {
        paused = false;
    }

    /**
     * Sets the started flag to true, indicating the game loop has started.
     */
    public void setStarted() {
        started = true;
    }
}
