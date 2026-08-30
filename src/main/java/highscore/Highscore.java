package highscore;

import game.Difficulty;

import java.time.LocalDateTime;

public record Highscore(
        long id,
        String playerName,
        Difficulty difficulty,
        int score,
        int maxCombo,
        LocalDateTime completedAt
) {
    public Highscore(String playerName, Difficulty difficulty, int score, int maxCombo) {
        this(0, playerName, difficulty, score, maxCombo, null);
    }
}
