package app;

import app.screen.*;
import controlP5.ControlP5;
import game.Beatmap;
import game.BeatmapLoader;
import game.Difficulty;
import game.GameManager;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;
import settings.DisplayManager;
import settings.GameSettings;

import java.util.EnumMap;
import java.util.Map;

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

    private DisplayManager displayManager;

    private ControlP5 cp5;
    private final Map<GameState, Screen> screens = new EnumMap<>(GameState.class);
    private Screen currentScreen;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

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

        screens.put(GameState.MENU, new MenuScreen(this, cp5));
        screens.put(GameState.GAME_CONFIG, new GameConfigScreen(this, cp5));
        screens.put(GameState.PLAYING, new PlayingScreen(this, cp5));
        screens.put(GameState.SETTINGS, new SettingsScreen(cp5));
        screens.put(GameState.TUTORIAL, new TutorialScreen(cp5));
        screens.put(GameState.HIGHSCORES, new HighscoresScreen(cp5));

        screens.values().forEach(Screen::hide);
        setState(GameState.MENU);
    }

    @Override
    public void draw() {
        background(0);

        if (currentScreen != null) {
            currentScreen.draw();
        }

        fill(255);

        cp5.draw();

        imageMode(CENTER);
        image(cursorImg, mouseX, mouseY, 100, 100);
    }

    public void setState(GameState state) {
        if (currentScreen != null) {
            currentScreen.hide();
        }

        this.state = state;
        currentScreen = screens.get(state);

        if (currentScreen != null) {
            currentScreen.show();
        }
    }

    // ==========================================
    // CONTROLP5 AUTOMATIC EVENT METHODS
    // These fire automatically based on button names
    // ==========================================

    public void gameConfig() {
        setState(GameState.GAME_CONFIG);
    }

    public void selectEasy() {
        selectDifficulty(Difficulty.EASY);
    }

    public void selectMedium() {
        selectDifficulty(Difficulty.MEDIUM);
    }

    public void selectHard() {
        selectDifficulty(Difficulty.HARD);
    }

    private void selectDifficulty(Difficulty difficulty) {
        GameConfigScreen configScreen = (GameConfigScreen) screens.get(GameState.GAME_CONFIG);
        configScreen.selectDifficulty(difficulty);

        if (currentGame != null) {
            currentGame.stop();
        }
        Beatmap beatmap = BeatmapLoader.load(this, difficulty);
        currentSong = new SoundFile(this, "songs/" + difficulty.name().toLowerCase() + ".mp3");
        currentGame = new GameManager(beatmap, currentSong);
    }

    public void startGame() {
        GameConfigScreen configScreen = (GameConfigScreen) screens.get(GameState.GAME_CONFIG);
        Difficulty difficulty = configScreen.getSelectedDifficulty();
        if (difficulty == null) {
            return;
        }

        selectedDifficulty = difficulty;
        if (currentGame == null || currentGame.getBeatmap().getDifficulty() != selectedDifficulty) {
            Beatmap beatmap = BeatmapLoader.load(this, selectedDifficulty);
            currentSong = new SoundFile(this, "songs/" + selectedDifficulty.name().toLowerCase() + ".mp3");
            currentGame = new GameManager(beatmap, currentSong);
        }

        ((PlayingScreen) screens.get(GameState.PLAYING)).setGameManager(currentGame);
        currentGame.start();
        println("STARTING GAME...");
        setState(GameState.PLAYING);
    }

    public void Highscores() {
        setState(GameState.HIGHSCORES);
    }

    public void Tutorial() {
        setState(GameState.TUTORIAL);
    }

    public void Settings() {
        setState(GameState.SETTINGS);
    }

    public void Exit() {
        exit();
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}
