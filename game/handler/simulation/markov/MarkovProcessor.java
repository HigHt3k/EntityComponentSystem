package game.handler.simulation.markov;

import com.ecs.entity.Entity;
import game.components.SimulationComponent;
import game.components.SimulationType;

import java.util.ArrayList;

/**
 * a generic markov chain processor class that can be called from anywhere
 */
public class MarkovProcessor {

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static int[] groupIds;


    /*

    ### example for a markov chain with conditions:
    1. "Win" Condition: At least 1 actuator with state CORRECT
    2. Actuator Condition: At least 1 CPU with CORRECT as input
    3. Actuator Condition: not more than 0 OUT_OF_CONTROL as input
    4. CPU Condition: At least 1 Sensor with CORRECT as input

    --> each component has a variable "correctSignalsNeeded" and a variable "outOfControlSignalsAccepted"
     */


    /**
     * generate the starting point
     *
     */
    public static void generateStartingNode() {

    }

    /**
     * start the chain
     */
    public static void startMarkov() {

    }

    /**
     * Print the markov chain
     */
    public static void printMarkov() {

    }

}
