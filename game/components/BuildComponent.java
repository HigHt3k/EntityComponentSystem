package game.components;

import com.IdGenerator;
import com.ecs.component.Component;

public class BuildComponent extends Component {
    private int amount;
    private float failureRatio;
    private SimulationType simulationType;

    @Override
    public void update() {

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
