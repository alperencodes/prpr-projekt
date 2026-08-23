package app;

import controlP5.*;
import game.Difficulty;
import game.GameManager;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;
import settings.DisplayManager;
import settings.GameSettings;

public class MainSketch extends PApplet {
    private GameState state = GameState.MENU;
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
    private DisplayManager displayManager;

    private ControlP5 cp5;

    private Group menuGroup;
    private Group gameConfigGroup;
    private Group playingGroup;
    private Group settingsGroup;
    private Group tutorialGroup;
    private Group highscoresGroup;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    private final int PINK_BASE = 0xFFFF69B4;  // Hot Pink
    private final int PINK_ACCENT = 0xFFFF1493; // Deep Pink

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        this.displayManager = new DisplayManager();
        final int frameRate = displayManager.isRateKnown() ? displayManager.getRefreshRate() : 60;
        frameRate(frameRate);

        noCursor();
        font = createFont("fonts/Torus.otf", 32);
        textFont(font);
        cursorImg = loadImage("images/cursor.png");
        imageMode(CENTER);

        cp5 = new ControlP5(this);
        cp5.setFont(font);
        cp5.setAutoDraw(false);

        menuGroup = cp5.addGroup("menuGroup").setLabel("");;
        gameConfigGroup = cp5.addGroup("gameConfigGroup").setLabel("");
        playingGroup = cp5.addGroup("playingGroup").setLabel("");
        settingsGroup = cp5.addGroup("settingsGroup").setLabel("");
        tutorialGroup = cp5.addGroup("tutorialGroup").setLabel("");
        highscoresGroup = cp5.addGroup("highscoresGroup").setLabel("");

        cp5.addTextarea("welcomeMessage")
                .setPosition(WIDTH / 2f - 100, 120)
                .setSize(300, 40)
                .setText("welcome to...")
                .hideScrollbar()
                .moveTo(menuGroup);

        int btnWidth = 175;
        int btnHeight = 40;
        float btnX = WIDTH / 2f - (btnWidth / 2f);

        int titleBtnWidth = 210;
        int titleBtnHeight = 60;
        float titleBtnX = WIDTH / 2f - (titleBtnWidth / 2f);

        cp5.addButton("gameConfig")
                .setLabel("KAIZEN!")
                .setPosition(titleBtnX, 175)
                .setSize(titleBtnWidth, titleBtnHeight)
                .setColorBackground(PINK_BASE)
                .moveTo(menuGroup)
                .getCaptionLabel()
                .toUpperCase(false)
                .setSize(40)
                .align(ControlP5.CENTER, ControlP5.CENTER);

        cp5.addButton("Highscores")
                .setPosition(btnX, 300)
                .setSize(btnWidth, btnHeight)
                .setColorBackground(0xFFFF69B4)
                .moveTo(menuGroup)
                .getCaptionLabel()
                .toUpperCase(false)
                .setSize(24)
                .align(ControlP5.CENTER, ControlP5.CENTER);

        cp5.addButton("Tutorial")
                .setPosition(btnX, 375)
                .setSize(btnWidth, btnHeight)
                .setColorBackground(0xFFFF69B4)
                .moveTo(menuGroup)
                .getCaptionLabel()
                .toUpperCase(false)
                .setSize(24)
                .align(ControlP5.CENTER, ControlP5.CENTER);

        cp5.addButton("Settings")
                .setPosition(btnX, 450)
                .setSize(btnWidth, btnHeight)
                .setColorBackground(0xFFFF69B4)
                .moveTo(menuGroup)
                .getCaptionLabel()
                .toUpperCase(false)
                .setSize(24)
                .align(ControlP5.CENTER, ControlP5.CENTER);

        cp5.addButton("Exit")
                .setPosition(btnX, 525)
                .setSize(btnWidth, btnHeight)
                .setColorBackground(0xFFFF69B4)
                .moveTo(menuGroup)
                .getCaptionLabel()
                .toUpperCase(false)
                .setSize(24)
                .align(ControlP5.CENTER, ControlP5.CENTER);
    }

    @Override
    public void draw() {
        background(0);

        animateUI();

        fill(255);

        cp5.draw();

        imageMode(CENTER);
        image(cursorImg, mouseX, mouseY, 100, 100);
    }

    private void animateUI() {
        if (state == GameState.MENU) {
            Button titleBtn = (Button) menuGroup.getController("gameConfig");
            if (titleBtn != null) {
                float wave = sin(millis() * 0.003f);
                float interpolationFactor = map(wave, -1f, 1f, 0f, 1f);
                int dynamicPinkValue = lerpColor(PINK_BASE, PINK_ACCENT, interpolationFactor);

                titleBtn.setColorBackground(dynamicPinkValue);
            }
        }
    }

    public void setState(GameState state) {
        this.state = state;

        menuGroup.hide();
        gameConfigGroup.hide();
        highscoresGroup.hide();
        playingGroup.hide();
        settingsGroup.hide();
        tutorialGroup.hide();

        switch(state) {
            case MENU -> menuGroup.show();
            case GAME_CONFIG -> gameConfigGroup.show();
            case HIGHSCORES -> highscoresGroup.show();
            case PLAYING -> playingGroup.show();
            case SETTINGS -> settingsGroup.show();
            case TUTORIAL -> tutorialGroup.show();
        }
    }

    // ==========================================
    // CONTROLP5 AUTOMATIC EVENT METHODS
    // These fire automatically based on button names
    // ==========================================

    public void gameConfig() {
        println("Play button clicked!");
        setState(GameState.GAME_CONFIG);
    }

    public void Play() {
        println("STARTING GAME...");
        setState(GameState.PLAYING);
    }

    public void Highscores() {
        println("Highscores button clicked!");
        setState(GameState.HIGHSCORES);
    }

    public void Tutorial() {
        println("Tutorial button clicked!");
        setState(GameState.TUTORIAL);
    }

    public void Settings() {
        println("Settings button clicked!");
        setState(GameState.SETTINGS);
    }

    public void Exit() {
        println("Exiting game...");
        exit();
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}