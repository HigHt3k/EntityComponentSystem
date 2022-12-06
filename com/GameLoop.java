package com;

/**
 * Standard Game Thread that updates all components of the Game at the given tickrate
 */
public class GameLoop extends Thread {
    private static final int DEFAULT_TICK_RATE = (int) (1000.0/144.0);
    private boolean paused = true;

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
    protected void process() {
        if(!paused) {
            Game.logic().update();
        }
        Game.input().handle();
        Game.frame().getRenderPanel().repaint();
    }

    protected void pause() {
        paused = true;
    }

    protected void unpause() {
        paused = false;
    }

}
