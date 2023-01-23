package game.handler.simulation.markov;

import java.util.ArrayList;

public class MarkovState {
    private MarkovState previous;
    private ArrayList<MarkovStateObject> markovStateObjects;
    private double stateProbability;

    public MarkovState(MarkovState previous, ArrayList<MarkovStateObject> markovStateObjects, double stateProbability) {
        this.previous = previous;
        this.markovStateObjects = markovStateObjects;
        this.stateProbability = stateProbability;
    }

    public double getStateProbability() {
        return stateProbability;
    }

    public ArrayList<MarkovStateObject> getMarkovStateObjects() {
        return markovStateObjects;
    }

    public void setStateProbability(double stateProbability) {
        this.stateProbability = stateProbability;
    }

    public MarkovState getPrevious() {
        return previous;
    }
}
