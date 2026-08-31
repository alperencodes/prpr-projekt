package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Textarea;
import processing.core.PApplet;

// this class is responsible for displaying the results of a round,
// including the score and maximum combo achieved by the player.
// It provides buttons to either play again or return to the main menu.
public class ResultsScreen extends AbstractScreen {
    private static final int PINK = 0xFFFF69B4;

    private final Textarea resultsText;

    public ResultsScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "resultsGroup");

        resultsText = cp5.addTextarea("roundResults")
                .setPosition(sketch.width / 2f - 180, 150)
                .setSize(360, 150)
                .setText("round complete!")
                .hideScrollbar()
                .moveTo(group);

        addButton(cp5, "playAgain", "play again", sketch.width / 2f - 90, 360);
        addButton(cp5, "returnToMenu", "main menu", sketch.width / 2f - 90, 430);
    }

    public void setResults(int score, int maximumCombo) {
        resultsText.setText("round complete!\n\nScore: " + score + "\nMax combo: " + maximumCombo);
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
