package app;

import game.Difficulty;
import game.GameManager;
import processing.core.PApplet;
import processing.sound.SoundFile;
import settings.GameSettings;

public class MainSketch extends PApplet {
    private GameState state;
    private GameManager currentGame;
    private GameSettings gameSettings;
    private SoundFile currentSong;
    private String playerName;
    private Difficulty selectedDifficulty;
    private long countdownStartMs;
    private int tutorialPage;

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}
