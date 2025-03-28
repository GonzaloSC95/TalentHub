package unir.reto.talenthub.entity;

public enum Enabled {
	
    DISABLED(0),
    ENABLED(1);

    private final int value;

    Enabled(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
