package app;

import app.screen.*;
import controlP5.ControlP5;
import game.Beatmap;
import game.BeatmapLoader;
import game.Difficulty;
import game.GameManager;
import game.GameConfig;
import game.Lane;
import highscore.Highscore;
import highscore.HighscoreRepository;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;
import settings.DisplayManager;
import settings.GameSettings;
import settings.GameSettingsStore;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// this class serves as the main entry point for the application,
// managing the overall game state and coordinating between different screens and game logic.
public class MainSketch extends PApplet {
    private GameState state = GameState.MENU;

    private GameManager currentGame;
    private GameSettings gameSettings;
    private GameSettingsStore gameSettingsStore;

    private SoundFile currentSong;
    private HighscoreRepository highscoreRepository;

    private PFont font;
    private PImage cursorImg;

    private DisplayManager displayManager;

    private ControlP5 cp5;
    private final Map<GameState, Screen> screens = new EnumMap<>(GameState.class);
    private final EnumSet<Lane> heldLanes = EnumSet.noneOf(Lane.class);
    private Screen currentScreen;

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        gameSettingsStore = new GameSettingsStore(Path.of("data", "settings.properties"));
        gameSettings = gameSettingsStore.load();
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

        Path databasePath = Path.of("data", "kaizen.db");
        try {
            Files.createDirectories(databasePath.toAbsolutePath().getParent());
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create highscore data directory", exception);
        }
        highscoreRepository = new HighscoreRepository(databasePath);
        highscoreRepository.initialize();

        screens.put(GameState.MENU, new MenuScreen(this, cp5));
        screens.put(GameState.GAME_CONFIG, new GameConfigScreen(this, cp5));
        screens.put(GameState.PLAYING, new PlayingScreen(this, cp5));
        screens.put(GameState.RESULTS, new ResultsScreen(this, cp5));
        screens.put(GameState.SETTINGS, new SettingsScreen(this, cp5, gameSettings));
        screens.put(GameState.TUTORIAL, new TutorialScreen(cp5));
        screens.put(GameState.HIGHSCORES, new HighscoresScreen(this, cp5, highscoreRepository));

        screens.values().forEach(Screen::hide);
        setState(GameState.MENU);
    }

    @Override
    public void draw() {
        background(0);

        if (state == GameState.PLAYING && currentGame != null) {
            currentGame.update();
            if (currentGame.isFinished()) {
                finishGame();
            }
        }

        if (currentScreen != null) {
            currentScreen.draw();
        }

        fill(255);

        cp5.draw();

        if (gameSettings.isShowFps()) {
            fill(255);
            textAlign(LEFT, TOP);
            textSize(18);
            text("FPS: " + Math.round(frameRate), 10, 10);
            textFont(font);
        }

        imageMode(CENTER);
        image(cursorImg, mouseX, mouseY, 100, 100);
    }

    public void setState(GameState state) {
        if (this.state == GameState.SETTINGS && state != GameState.SETTINGS) {
            gameSettingsStore.save(gameSettings);
        }
        if (currentScreen != null) {
            currentScreen.hide();
        }

        this.state = state;
        surface.setTitle("KAIZEN! - " + state.getScreenName());
        if (state != GameState.PLAYING) {
            heldLanes.clear();
        }
        currentScreen = screens.get(state);

        if (currentScreen != null) {
            currentScreen.show();
        }
    }

    @Override
    public void keyPressed() {
        if (state != GameState.PLAYING || currentGame == null) {
            return;
        }

        Lane lane = Lane.fromKey(key);
        if (lane != null && heldLanes.add(lane)) {
            currentGame.hit(lane);
        }
    }

    @Override
    public void keyReleased() {
        Lane lane = Lane.fromKey(key);
        if (lane != null) {
            heldLanes.remove(lane);
        }
    }

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
    }

    @Override
    public void mouseReleased() {
        if (state == GameState.SETTINGS) {
            gameSettingsStore.save(gameSettings);
        }
    }

    public void startGame() {
        GameConfigScreen configScreen = (GameConfigScreen) screens.get(GameState.GAME_CONFIG);
        Difficulty difficulty = configScreen.getSelectedDifficulty();
        String playerName = configScreen.getValidPlayerName();
        if (difficulty == null || playerName == null) {
            return;
        }

        if (currentGame != null) {
            currentGame.stop();
        }
        GameConfig config = new GameConfig(difficulty, playerName);
        Beatmap beatmap = BeatmapLoader.load(this, difficulty);
        currentSong = new SoundFile(this, "songs/" + difficulty.name().toLowerCase() + ".mp3");
        currentGame = new GameManager(beatmap, currentSong, config, gameSettings);

        ((PlayingScreen) screens.get(GameState.PLAYING)).setGameManager(currentGame);
        currentGame.start();
        println("STARTING GAME...");
        setState(GameState.PLAYING);
    }

    private void finishGame() {
        if (!currentGame.markResultSaved()) {
            return;
        }
        GameConfig config = currentGame.getSession().getConfig();
        highscoreRepository.save(new Highscore(
                config.playerName(),
                config.difficulty(),
                currentGame.getScoreTracker().getScore(),
                currentGame.getScoreTracker().getMaximumCombo()
        ));
        ResultsScreen resultsScreen = (ResultsScreen) screens.get(GameState.RESULTS);
        resultsScreen.setResults(
                currentGame.getScoreTracker().getScore(),
                currentGame.getScoreTracker().getMaximumCombo()
        );
        setState(GameState.RESULTS);
    }

    public void playAgain() {
        setState(GameState.GAME_CONFIG);
    }

    public void returnToMenu() {
        setState(GameState.MENU);
    }

    public void highscoresMainMenu() {
        returnToMenu();
    }

    public void gameConfigMainMenu() {
        returnToMenu();
    }

    public void resultsMainMenu() {
        returnToMenu();
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

    public void songVolume(float percent) {
        gameSettings.setSongVolume(percent / 100f);
        if (currentSong != null) {
            currentSong.amp(gameSettings.getSongVolume());
        }
    }

    public void showFps(boolean enabled) {
        gameSettings.setShowFps(enabled);
        gameSettingsStore.save(gameSettings);
    }

    public void resetSettings() {
        gameSettings.resetToDefaults();
        if (currentSong != null) {
            currentSong.amp(gameSettings.getSongVolume());
        }
        ((SettingsScreen) screens.get(GameState.SETTINGS)).syncControlsFromSettings();
        gameSettingsStore.save(gameSettings);
    }

    public void Exit() {
        exit();
    }

    public static void main(String[] args) {
        PApplet.main(MainSketch.class, args);
    }
}
