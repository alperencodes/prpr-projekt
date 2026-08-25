package game;

public enum Lane {
    D,
    F,
    J,
    K;

    public static Lane fromKey(char key) {
        return switch (Character.toUpperCase(key)) {
            case 'D' -> D;
            case 'F' -> F;
            case 'J' -> J;
            case 'K' -> K;
            default -> null;
        };
    }
}
