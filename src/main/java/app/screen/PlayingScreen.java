package app.screen;

import controlP5.ControlP5;
import game.Beatmap;
import game.GameManager;
import game.Lane;
import game.Note;
import processing.core.PApplet;

public class PlayingScreen extends AbstractScreen {
    private static final int LANE_COUNT = 4;
    private static final float LANE_WIDTH = 110f;
    private static final float NOTE_HEIGHT = 36f;
    private static final float SPAWN_Y = -NOTE_HEIGHT;
    private static final float AFTER_HIT_VISIBLE_MS = 250f;

    private final PApplet sketch;
    private GameManager gameManager;

    public PlayingScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "playingGroup");
        this.sketch = sketch;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void draw() {
        if (gameManager == null) {
            return;
        }

        float laneAreaX = (sketch.width - LANE_COUNT * LANE_WIDTH) / 2f;
        float receptorY = sketch.height - 105f;
        drawLanes(laneAreaX, receptorY);
        drawNotes(laneAreaX, receptorY);
    }

    private void drawLanes(float laneAreaX, float receptorY) {
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.stroke(90);

        for (int i = 0; i < LANE_COUNT; i++) {
            float x = laneAreaX + i * LANE_WIDTH;
            sketch.fill(22);
            sketch.rect(x, 0, LANE_WIDTH, sketch.height);

            sketch.fill(255, 105, 180);
            sketch.rect(x + 8, receptorY, LANE_WIDTH - 16, NOTE_HEIGHT);

            sketch.fill(255);
            sketch.text(Lane.values()[i].name(), x + LANE_WIDTH / 2f, receptorY + NOTE_HEIGHT / 2f);
        }
    }

    private void drawNotes(float laneAreaX, float receptorY) {
        Beatmap beatmap = gameManager.getBeatmap();
        double songTimeMs = gameManager.getSmoothSongTimeMs();
        long approachTimeMs = beatmap.getApproachTimeMs();

        sketch.noStroke();
        sketch.fill(255, 20, 147);
        for (Note note : beatmap.getNotes()) {
            double timeUntilHitMs = note.getHitTimeMs() - songTimeMs;
            if (!note.isPending()
                    || timeUntilHitMs > approachTimeMs
                    || timeUntilHitMs < -AFTER_HIT_VISIBLE_MS) {
                continue;
            }

            float progress = 1f - (float) timeUntilHitMs / approachTimeMs;
            float y = PApplet.lerp(SPAWN_Y, receptorY, progress);
            float x = laneAreaX + note.getLane().ordinal() * LANE_WIDTH + 8;
            sketch.rect(x, y, LANE_WIDTH - 16, NOTE_HEIGHT);
        }
    }
}
