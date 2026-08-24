package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import game.Difficulty;
import processing.core.PApplet;

public class GameConfigScreen extends AbstractScreen {
    private static final int PINK_BASE = 0xFFFF69B4;
    private static final int PINK_SELECTED = 0xFFFF1493;

    private final Button easyButton;
    private final Button mediumButton;
    private final Button hardButton;
    private final Button startButton;
    private Difficulty selectedDifficulty;

    public GameConfigScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "gameConfigGroup");

        cp5.addTextarea("difficultyPrompt")
                .setPosition(sketch.width / 2f - 180, 140)
                .setSize(360, 50)
                .setText("Choose a difficulty")
                .hideScrollbar()
                .moveTo(group);

        int buttonWidth = 175;
        int buttonHeight = 45;
        float buttonX = sketch.width / 2f - buttonWidth / 2f;

        easyButton = addButton(cp5, "selectEasy", "Easy", buttonX, 230, buttonWidth, buttonHeight);
        mediumButton = addButton(cp5, "selectMedium", "Medium", buttonX, 300, buttonWidth, buttonHeight);
        hardButton = addButton(cp5, "selectHard", "Hard", buttonX, 370, buttonWidth, buttonHeight);
        startButton = addButton(cp5, "startGame", "Start game", buttonX, 470, buttonWidth, buttonHeight);
        startButton.hide();
    }

    public void selectDifficulty(Difficulty difficulty) {
        selectedDifficulty = difficulty;
        easyButton.setColorBackground(difficulty == Difficulty.EASY ? PINK_SELECTED : PINK_BASE);
        mediumButton.setColorBackground(difficulty == Difficulty.MEDIUM ? PINK_SELECTED : PINK_BASE);
        hardButton.setColorBackground(difficulty == Difficulty.HARD ? PINK_SELECTED : PINK_BASE);
        startButton.show();
    }

    public Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    @Override
    public void show() {
        super.show();
        if (selectedDifficulty == null) {
            startButton.hide();
        }
    }

    private Button addButton(ControlP5 cp5, String name, String label, float x, int y, int width, int height) {
        Button button = cp5.addButton(name)
                .setLabel(label)
                .setPosition(x, y)
                .setSize(width, height)
                .setColorBackground(PINK_BASE)
                .moveTo(group);
        button.getCaptionLabel()
                .toUpperCase(false)
                .setSize(24)
                .align(ControlP5.CENTER, ControlP5.CENTER);
        return button;
    }
}
