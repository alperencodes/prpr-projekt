package app;

import game.Difficulty;
import game.GameManager;
import processing.core.PApplet;
import processing.core.PFont;
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
    private PFont font;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        font = createFont("Torus", 32);
        textFont(font);
    }

    @Override
    public void draw() {
        background(0);

        // drawLanes();
        // drawNotes();

        fill(255);
        text("改善\nwelcome to kaizen!", (float) WIDTH / 2, 120);
        textAlign(CENTER);
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}
