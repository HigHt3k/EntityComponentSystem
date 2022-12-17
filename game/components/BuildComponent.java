package game.components;

import com.ecs.component.Component;

public class BuildComponent extends Component {
    private int amount;
    private float failureRatio;

    @Override
    public void update() {

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
