package game;

import java.util.Objects;

public record GameConfig(Difficulty difficulty, String playerName) {
    public static final int MAX_PLAYER_NAME_LENGTH = 20;

    public GameConfig {
        Objects.requireNonNull(difficulty, "difficulty");
        Objects.requireNonNull(playerName, "playerName");
        playerName = playerName.trim();
        if (playerName.isEmpty()) {
            throw new IllegalArgumentException("Player name must not be blank");
        }
        if (playerName.length() > MAX_PLAYER_NAME_LENGTH) {
            throw new IllegalArgumentException("Player name must be at most " + MAX_PLAYER_NAME_LENGTH + " characters");
        }
    }
}
