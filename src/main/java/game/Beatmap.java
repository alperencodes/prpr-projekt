package game;

import java.util.List;

public record Beatmap(String song, String artist, Difficulty difficulty, int bpm, long approachTimeMs,
                      List<Note> notes) {
    public Beatmap(String song, String artist, Difficulty difficulty, int bpm,
                   long approachTimeMs, List<Note> notes) {
        this.song = song;
        this.artist = artist;
        this.difficulty = difficulty;
        this.bpm = bpm;
        this.approachTimeMs = approachTimeMs;
        this.notes = List.copyOf(notes);
    }
}
