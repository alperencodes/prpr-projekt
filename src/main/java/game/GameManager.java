package game;

import processing.sound.SoundFile;
import settings.GameSettings;

// this class manages the state of a gameplay session, including the beatmap, song, and score tracking
public class GameManager {
    private static final double MAX_SMOOTHING_OFFSET_MS = 20.0;
    private final Beatmap beatmap;
    private final SoundFile song;
    private final GameplaySession session;
    private final GameSettings gameSettings;
    private double smoothedSongTimeMs;
    private long lastClockReadNanos;
    private boolean started;
    private boolean finished;
    private boolean resultSaved;

    // this class is responsible for managing the state of a gameplay session,
    // including the beatmap, song, and score tracking.
    // It provides methods to start and stop the game, update the game state,
    // handle player input, and retrieve relevant information about the current session.
    public GameManager(Beatmap beatmap, SoundFile song, GameConfig config, GameSettings gameSettings) {
        this.beatmap = beatmap;
        this.song = song;
        this.session = new GameplaySession(beatmap, config);
        this.gameSettings = gameSettings;
    }

    public void start() {
        song.cue(0);
        song.play();
        song.amp(gameSettings.getSongVolume());
        started = true;
        finished = false;
        
        smoothedSongTimeMs = 0.0;
        lastClockReadNanos = System.nanoTime();
    }

    public void stop() {
        song.stop();
    }

    public void update() {
        if (!started || finished) {
            return;
        }

        long songTimeMs = getSongTimeMs();
        session.update(songTimeMs);
        if (session.isComplete()) {
            finished = true;
            song.stop();
        } else if (!song.isPlaying()
                && songTimeMs >= Math.round(song.duration() * 1000.0) - 100) {
            session.missAllPendingNotes();
            finished = true;
        }
    }

    public HitJudgement hit(Lane lane) {
        if (!started || finished) {
            return null;
        }
        return session.hit(lane, getSongTimeMs());
    }

    public long getSongTimeMs() {
        return Math.round(song.position() * 1000.0);
    }

    public double getSmoothSongTimeMs() {
        double rawSongTimeMs = song.position() * 1000.0;
        long nowNanos = System.nanoTime();

        if (lastClockReadNanos == 0L || !song.isPlaying()) {
            smoothedSongTimeMs = rawSongTimeMs;
            lastClockReadNanos = nowNanos;
            return smoothedSongTimeMs;
        }

        double elapsedMs = (nowNanos - lastClockReadNanos) / 1_000_000.0;
        double predictedTimeMs = smoothedSongTimeMs + elapsedMs;
        double minimumTimeMs = Math.max(0.0, rawSongTimeMs - MAX_SMOOTHING_OFFSET_MS);
        double maximumTimeMs = rawSongTimeMs + MAX_SMOOTHING_OFFSET_MS;

        smoothedSongTimeMs = Math.clamp(predictedTimeMs, minimumTimeMs, maximumTimeMs);
        lastClockReadNanos = nowNanos;
        return smoothedSongTimeMs;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    public SoundFile getSong() {
        return song;
    }

    public ScoreTracker getScoreTracker() {
        return session.getScoreTracker();
    }

    public HitJudgement getLatestJudgement() {
        return session.getLatestJudgement();
    }

    public boolean isFinished() {
        return finished;
    }

    public GameplaySession getSession() {
        return session;
    }

    public boolean markResultSaved() {
        if (!finished || resultSaved) {
            return false;
        }
        resultSaved = true;
        return true;
    }
}
