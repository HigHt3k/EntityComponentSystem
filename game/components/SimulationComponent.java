package game.components;

import com.ecs.component.Component;

public class SimulationComponent extends Component {
    private float failureRatio;
    private SimulationType simulationType;

    @Override
    public void update() {

    }

    public void setFailureRatio(float failureRatio) {
        this.failureRatio = failureRatio;
    }

    public float getFailureRatio() {
        return failureRatio;
    }

    public SimulationType getSimulationType() {
        return simulationType;
    }

    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
    }
}
