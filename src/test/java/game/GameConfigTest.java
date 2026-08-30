package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameConfigTest {
    @Test
    void trimsPlayerName() {
        GameConfig config = new GameConfig(Difficulty.MEDIUM, "  Player  ");

        assertEquals("Player", config.playerName());
    }

    @Test
    void rejectsBlankAndOverlongPlayerNames() {
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(Difficulty.EASY, "   "));
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(
                Difficulty.HARD,
                "x".repeat(GameConfig.MAX_PLAYER_NAME_LENGTH + 1)
        ));
    }
}
