package game;

import processing.sound.SoundFile;
import processing.sound.Sound;

// this class manages the state of a gameplay session, including the beatmap, song, and score tracking
public class GameManager {
    private static final double MAX_SMOOTHING_OFFSET_MS = 20.0;
    private static final float SONG_VOLUME = 0.05f;

    private final Beatmap beatmap;
    private final SoundFile song;
    private final GameplaySession session;
    private double smoothedSongTimeMs;
    private long lastClockReadNanos;
    private boolean started;
    private boolean finished;

    // this class is responsible for managing the state of a gameplay session,
    // including the beatmap, song, and score tracking.
    // It provides methods to start and stop the game, update the game state,
    // handle player input, and retrieve relevant information about the current session.
    public GameManager(Beatmap beatmap, SoundFile song) {
        this.beatmap = beatmap;
        this.song = song;
        this.session = new GameplaySession(beatmap);
    }

    public void start() {
        song.cue(0);
        Sound.volume(SONG_VOLUME);
        song.play();
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

        session.update(getSongTimeMs());
        if (session.isComplete()) {
            finished = true;
            song.stop();
        } else if (!song.isPlaying()
                && getSongTimeMs() >= Math.round(song.duration() * 1000.0) - 100) {
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

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }
}
