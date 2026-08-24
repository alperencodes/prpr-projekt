package game;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class BeatmapLoader {
    private BeatmapLoader() {
    }

    public static Beatmap load(PApplet sketch, Difficulty difficulty) {
        String path = "beatmaps/" + difficulty.name().toLowerCase() + ".json";
        JSONObject json = sketch.loadJSONObject(path);
        if (json == null) {
            throw new IllegalArgumentException("Could not load beat map: " + path);
        }

        Difficulty fileDifficulty = Difficulty.valueOf(json.getString("difficulty"));
        if (fileDifficulty != difficulty) {
            throw new IllegalArgumentException("Beatmap difficulty does not match" + difficulty);
        }

        JSONArray noteData = json.getJSONArray("notes");
        List<Note> notes = new ArrayList<>(noteData.size());
        for (int i = 0; i < noteData.size(); i++) {
            JSONObject note = noteData.getJSONObject(i);
            notes.add(new Note(Lane.valueOf(note.getString("lane")), note.getLong("time")));
        }

        return new Beatmap(
                json.getString("song"),
                json.getString("artist"),
                fileDifficulty,
                json.getInt("bpm"),
                json.getLong("approachTimeMs"),
                notes
        );
    }
}
