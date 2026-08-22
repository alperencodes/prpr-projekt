package app;

import controlP5.ControlP5;
import controlP5.Textarea;
import game.Difficulty;
import game.GameManager;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
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
    private PImage cursorImg;
    private Textarea title;

    // controlp5 instanze
    private ControlP5 cp5;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        noCursor();
        font = createFont("fonts/Torus.otf", 32);
        textFont(font);
        cursorImg = loadImage("images/cursor.png");
        imageMode(CENTER);

        // initialisier cp5
        cp5 = new ControlP5(this);
        cp5.setFont(font);
        cp5.setAutoDraw(false);

        cp5.addTextarea("welcome to kaizen!")
                .setPosition(WIDTH / 2f - 150, 120) // Textareas align from top-left, adjust X accordingly
                .setSize(300, 50)
                .setText("welcome to kaizen!")
                .hideScrollbar();

        float btnX = WIDTH / 2f - 100; // Shifted left slightly to center a 200px wide button
        int btnWidth = 200;
        int btnHeight = 50;

        cp5.addButton("Play")
                .setPosition(btnX, 200)
                .setSize(btnWidth, btnHeight);

        cp5.addButton("Highscores")
                .setPosition(btnX, 275)
                .setSize(btnWidth, btnHeight);

        cp5.addButton("Tutorial")
                .setPosition(btnX, 350)
                .setSize(btnWidth, btnHeight);

        cp5.addButton("Settings")
                .setPosition(btnX, 425)
                .setSize(btnWidth, btnHeight);

        cp5.addButton("Exit")
                .setPosition(btnX, 500)
                .setSize(btnWidth, btnHeight);
    }

    @Override
    public void draw() {
        background(0);

        // drawLanes();
        // drawNotes();

        fill(255);

        // manually drawing buttons first so that mouse cursor is on the top layer
        cp5.draw();

        imageMode(CENTER);
        image(cursorImg, mouseX, mouseY, 100, 100);
    }

    // ==========================================
    // CONTROLP5 AUTOMATIC EVENT METHODS
    // These fire automatically based on button names
    // ==========================================

    public void Play() {
        println("Play button clicked!");
        // Switch state to gameplay, hide menu UI: cp5.hide();
    }

    public void Highscores() {
        println("Highscores button clicked!");
    }

    public void Tutorial() {
        println("Tutorial button clicked!");
    }

    public void Settings() {
        println("Settings button clicked!");
    }

    public void Exit() {
        println("Exiting game...");
        exit();
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}