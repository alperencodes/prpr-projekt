package game;

// this class is responsible for tracking the player's score,
// current combo, and maximum combo achieved during a round.
// It updates these values based on the judgement of each hit (e.g., perfect, good, miss)
// and provides methods to retrieve the current score, combo, and maximum combo.
public class ScoreTracker {
    private int score;
    private int combo;
    private int maximumCombo;

    public void register(HitJudgement judgement) {
        score += judgement.getPoints();
        if (judgement == HitJudgement.MISS) {
            combo = 0;
            return;
        }

        combo++;
        maximumCombo = Math.max(maximumCombo, combo);
    }

    public int getScore() {
        return score;
    }

    public int getCombo() {
        return combo;
    }

    public int getMaximumCombo() {
        return maximumCombo;
    }
}
