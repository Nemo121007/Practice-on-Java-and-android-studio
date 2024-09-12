package Signs;

public enum Signs {
    ROCK(1),
    SCISSORS(2),
    PAPER(3);

    private final int value;

    Signs(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Signs fromValue(int value) {
        for (Signs sign : values()) {
            if (sign.value == value) {
                return sign;
            }
        }
        throw new IllegalArgumentException("Invalid sign value: " + value);
    }
}