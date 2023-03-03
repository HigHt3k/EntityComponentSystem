package engine;

/**
 * Standard Game Thread that updates all components of the Game at the given tickrate
 */
public class GameLoop extends Thread {
    private static final int DEFAULT_TICK_RATE = (int) (1000.0/240.0);
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
    protected void process() {
        if(started) {
            // handle inputs
            Game.input().handle();
            // handle systems
            Game.system().handle();
            // update the scene
            Game.scene().current().update();
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