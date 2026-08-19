package app;

import game.Difficulty;
import game.GameManager;
import processing.core.PApplet;
import processing.core.PFont;
import processing.sound.SoundFile;
import settings.GameSettings;
import ui.Button;

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

    private Button playButton = new Button("Play", WIDTH / 2f - 50, 200, 100, 50);
    private Button highscoresButton = new Button("Highscores", WIDTH / 2f - 75, 275, 150, 50);
    private Button tutorialButton = new Button("Tutorial", WIDTH / 2f - 75, 350, 150, 50);
    private Button settingsButton = new Button("Settings", WIDTH / 2f - 75, 425, 150, 50);
    private Button exitButton = new Button("Exit", WIDTH / 2f - 50, 500, 100, 50);

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
        drawButtons();

        fill(255);
        text("改善\nwelcome to kaizen!", (float) WIDTH / 2, 120);
        textAlign(CENTER);
    }

    public void drawButtons() {
        playButton.draw(this);
        highscoresButton.draw(this);
        tutorialButton.draw(this);
        settingsButton.draw(this);
        exitButton.draw(this);
    }

    @Override
    public void mousePressed() {
        if (exitButton.contains(mouseX, mouseY)) {
            exit();
        }
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}
