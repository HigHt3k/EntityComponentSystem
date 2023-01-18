package game.components;

import com.ecs.component.Component;
import game.handler.simulation.SimulationType;

public class BuildComponent extends Component {
    private int amount;
    private float failureRatio;
    private SimulationType simulationType;
    private int portId;
    private int outOfControlSignalsAccepted;
    private int correctSignalsNeeded;

    @Override
    public void update() {

    }

    public int getOutOfControlSignalsAccepted() {
        return outOfControlSignalsAccepted;
    }

    public int getCorrectSignalsNeeded() {
        return correctSignalsNeeded;
    }

    public void setOutOfControlSignalsAccepted(int outOfControlSignalsAccepted) {
        this.outOfControlSignalsAccepted = outOfControlSignalsAccepted;
    }

    public void setCorrectSignalsNeeded(int correctSignalsNeeded) {
        this.correctSignalsNeeded = correctSignalsNeeded;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public int getPortId() {
        return portId;
    }

    public SimulationType getSimulationType() {
        return simulationType;
    }

    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void addToAmount() {
        amount++;
    }

    public void subtractFromAmount() {
        amount--;
    }

    public void setFailureRatio(float failureRatio) {
        this.failureRatio = failureRatio;
    }

    public float getFailureRatio() {
        return failureRatio;
    }
}
