package highscore;

import game.Difficulty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HighscoreRepositoryTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void savesRequiredValuesAndOrdersByScoreThenMaxCombo() {
        HighscoreRepository repository = new HighscoreRepository(temporaryDirectory.resolve("test.db"));
        repository.initialize();

        repository.save(new Highscore("Lower", Difficulty.EASY, 500, 20));
        repository.save(new Highscore("Winner", Difficulty.HARD, 1_000, 10));
        repository.save(new Highscore("Combo", Difficulty.MEDIUM, 500, 30));

        List<Highscore> scores = repository.findTop(10);

        assertEquals(List.of("Winner", "Combo", "Lower"), scores.stream().map(Highscore::playerName).toList());
        Highscore winner = scores.getFirst();
        assertNotEquals(0, winner.id());
        assertEquals(Difficulty.HARD, winner.difficulty());
        assertEquals(1_000, winner.score());
        assertEquals(10, winner.maxCombo());
        assertNotNull(winner.completedAt());
    }
}
