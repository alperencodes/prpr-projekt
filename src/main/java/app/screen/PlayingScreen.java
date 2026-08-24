package app.screen;

import controlP5.ControlP5;
import controlP5.Textarea;
import game.Difficulty;
import processing.core.PApplet;

public class PlayingScreen extends AbstractScreen {
    private final Textarea statusText;

    public PlayingScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "playingGroup");

        statusText = cp5.addTextarea("playingStatus")
                .setPosition(sketch.width / 2f - 180, 160)
                .setSize(360, 50)
                .setText("Playing")
                .hideScrollbar()
                .moveTo(group);
    }

    public void setDifficulty(Difficulty difficulty) {
        String name = difficulty.name().toLowerCase();
        statusText.setText("Playing - " + Character.toUpperCase(name.charAt(0)) + name.substring(1));
    }
}
