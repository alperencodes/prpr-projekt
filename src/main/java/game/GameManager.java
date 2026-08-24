package game;

import processing.sound.SoundFile;
import processing.sound.Sound;

public class GameManager {
    private static final double MAX_SMOOTHING_OFFSET_MS = 20.0;
    private static final float SONG_VOLUME = 0.05f;

    private final Beatmap beatmap;
    private final SoundFile song;
    private double smoothedSongTimeMs;
    private long lastClockReadNanos;

    public GameManager(Beatmap beatmap, SoundFile song) {
        this.beatmap = beatmap;
        this.song = song;
    }

    public void start() {
        song.cue(0);
        Sound.volume(SONG_VOLUME);
        song.play();
        
        smoothedSongTimeMs = 0.0;
        lastClockReadNanos = System.nanoTime();
    }

    public void stop() {
        song.stop();
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
}
