package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import processing.core.PApplet;

// this class is responsible for displaying the results of a round,
// including the score and maximum combo achieved by the player.
public class ResultsScreen extends AbstractScreen {
    private static final int PINK = 0xFFFF69B4;

    private final PApplet sketch;
    private int score;
    private int maximumCombo;

    public ResultsScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "resultsGroup");
        this.sketch = sketch;

        addButton(cp5, "playAgain", "play again", sketch.width / 2f - 90, 360);
        addButton(cp5, "resultsMainMenu", "main menu", sketch.width / 2f - 90, 430);
    }

    public void setResults(int score, int maximumCombo) {
        this.score = score;
        this.maximumCombo = maximumCombo;
    }

    @Override
    public void draw() {
        sketch.pushStyle();
        sketch.fill(255);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.textSize(30);
        sketch.text("round complete!", sketch.width / 2f, 150);
        sketch.textSize(22);
        sketch.text("score: " + score, sketch.width / 2f, 220);
        sketch.text("max combo: " + maximumCombo, sketch.width / 2f, 260);
        sketch.popStyle();
    }

    private void addButton(ControlP5 cp5, String name, String label, float x, float y) {
        Button button = cp5.addButton(name)
                .setLabel(label)
                .setPosition(x, y)
                .setSize(180, 45)
                .setColorBackground(PINK)
                .moveTo(group);
        button.getCaptionLabel()
                .toUpperCase(false)
                .setSize(22)
                .align(ControlP5.CENTER, ControlP5.CENTER);
    }
}
