package game;

// this class represents the judgement of a hit in the game,
// based on the timing difference between the note's hit time and the player's input
public enum HitJudgement {
    PERFECT_300(45, 300, "300"),
    GREAT_200(90, 200, "200"),
    GOOD_100(135, 100, "100"),
    OK_50(180, 50, "50"),
    MISS(Long.MAX_VALUE, 0, "Miss");

    public static final long MAX_HIT_WINDOW_MS = OK_50.windowMs;

    private final long windowMs;
    private final int points;
    private final String label;

    HitJudgement(long windowMs, int points, String label) {
        this.windowMs = windowMs;
        this.points = points;
        this.label = label;
    }

    public static HitJudgement fromTimingDifference(long differenceMs) {
        long absoluteDifference = Math.abs(differenceMs);
        for (HitJudgement judgement : values()) {
            if (absoluteDifference <= judgement.windowMs) {
                return judgement;
            }
        }
        return MISS;
    }

    public int getPoints() {
        return points;
    }

    public String getLabel() {
        return label;
    }
}
