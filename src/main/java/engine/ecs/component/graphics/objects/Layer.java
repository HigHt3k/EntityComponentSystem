package engine.ecs.component.graphics.objects;

public enum Layer {
    BACKGROUND(0),
    GAMELAYER1(1),
    GAMELAYER2(2),
    GAMELAYER3(3),
    UI(4),
    UI_FRONT(5),
    HOVER(6),
    TOOLTIP(7),
    UI_HOVER(8),
    CURSOR(9);

    private final int value;

    Layer(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
