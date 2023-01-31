package game.handler.simulation.markov;

import game.handler.simulation.SimulationType;

import java.util.ArrayList;

public class MarkovState {
    private MarkovState previous;
    private ArrayList<MarkovStateObject> markovStateObjects;
    private double stateProbability;

    public MarkovState(MarkovState previous, ArrayList<MarkovStateObject> markovStateObjects, double stateProbability) {
        this.previous = previous;
        this.markovStateObjects = new ArrayList<>(markovStateObjects);
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

    public String selfToText() {
        StringBuilder out = new StringBuilder("|");
        for(MarkovStateObject object : markovStateObjects) {
            if(object.getType() == SimulationType.CABLE) {
                continue;
            }
            switch(object.getState()) {

                case CORRECT -> {
                    out.append("C|");
                }
                case FAIL -> {
                    out.append("F|");
                }
                case PASSIVE -> {
                    out.append("P|");
                }
                case OUT_OF_CONTROL -> {
                    out.append("O|");
                }
                case INOPERATIVE -> {
                    out.append("I|");
                }
            }
        }
        return out.toString();
    }
}
