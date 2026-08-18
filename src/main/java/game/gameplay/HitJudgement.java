package game.gameplay;

public enum HitJudgement {
    PERFECT,    // < 50ms
    GREAT,      // < 100ms
    GOOD,       // < 200ms
    OKAY,       // <= 300ms
    MISS        // > 300ms
}
