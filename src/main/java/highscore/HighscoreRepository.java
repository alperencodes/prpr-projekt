package highscore;

import game.Difficulty;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HighscoreRepository {
    private static final DateTimeFormatter DATABASE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String jdbcUrl;

    public HighscoreRepository(Path databasePath) {
        this("jdbc:sqlite:" + databasePath.toAbsolutePath());
    }

    public HighscoreRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS highscores (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player_name TEXT NOT NULL,
                    difficulty TEXT NOT NULL,
                    score INTEGER NOT NULL,
                    max_combo INTEGER NOT NULL,
                    completed_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """;
        try (Connection connection = open(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw persistenceFailure("initialize highscore database", exception);
        }
    }

    public void save(Highscore highscore) {
        String sql = "INSERT INTO highscores (player_name, difficulty, score, max_combo) VALUES (?, ?, ?, ?)";
        try (Connection connection = open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, highscore.playerName());
            statement.setString(2, highscore.difficulty().name());
            statement.setInt(3, highscore.score());
            statement.setInt(4, highscore.maxCombo());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw persistenceFailure("save highscore", exception);
        }
    }

    public List<Highscore> findTop(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be positive");
        }
        String sql = """
                SELECT id, player_name, difficulty, score, max_combo, completed_at
                FROM highscores
                ORDER BY score DESC, max_combo DESC, id ASC
                LIMIT ?
                """;
        List<Highscore> highscores = new ArrayList<>();
        try (Connection connection = open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    highscores.add(new Highscore(
                            results.getLong("id"),
                            results.getString("player_name"),
                            Difficulty.valueOf(results.getString("difficulty")),
                            results.getInt("score"),
                            results.getInt("max_combo"),
                            LocalDateTime.parse(results.getString("completed_at"), DATABASE_TIME)
                    ));
                }
            }
        } catch (SQLException exception) {
            throw persistenceFailure("read highscores", exception);
        }
        return highscores;
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    private IllegalStateException persistenceFailure(String action, SQLException cause) {
        return new IllegalStateException("Could not " + action + " using " + jdbcUrl, cause);
    }
}
