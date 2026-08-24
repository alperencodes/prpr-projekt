package game;

import java.util.List;

public class Beatmap {
    private final String song;
    private final String artist;
    private final Difficulty difficulty;
    private final int bpm;
    private final long approachTimeMs;
    private final List<Note> notes;

    public Beatmap(String song, String artist, Difficulty difficulty, int bpm,
                   long approachTimeMs, List<Note> notes) {
        this.song = song;
        this.artist = artist;
        this.difficulty = difficulty;
        this.bpm = bpm;
        this.approachTimeMs = approachTimeMs;
        this.notes = List.copyOf(notes);
    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getBpm() {
        return bpm;
    }

    public long getApproachTimeMs() {
        return approachTimeMs;
    }

    public List<Note> getNotes() {
        return notes;
    }
}
