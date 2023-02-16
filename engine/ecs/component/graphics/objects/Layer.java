package engine.ecs.component.graphics.objects;

public enum Layer {
    BACKGROUND(0),
    GAMELAYER1(1),
    GAMELAYER2(2),
    GAMELAYER3(3),
    UI(4),
    HOVER(5),
    TOOLTIP(6),
    CURSOR(7);

    private final int value;

    Layer(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
