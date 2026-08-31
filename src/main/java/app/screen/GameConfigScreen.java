package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Textfield;
import game.GameConfig;
import game.Difficulty;
import processing.core.PApplet;

public class GameConfigScreen extends AbstractScreen {
    private static final int PINK_BASE = 0xFFFF69B4;
    private static final int PINK_SELECTED = 0xFFFF1493;

    private final PApplet sketch;
    private final Button easyButton;
    private final Button mediumButton;
    private final Button hardButton;
    private final Button startButton;
    private final Textfield playerNameField;
    private String validationMessage = "";
    private Difficulty selectedDifficulty;

    public GameConfigScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "gameConfigGroup");
        this.sketch = sketch;

        playerNameField = cp5.addTextfield("playerNameInput")
                .setPosition(sketch.width / 2f - 180, 120)
                .setSize(360, 42)
                .setAutoClear(false)
                .moveTo(group);
        playerNameField.getCaptionLabel().setText("");
        playerNameField.getValueLabel().hide();
        // ControlP5 still draws its own caret even with the value label hidden.
        // It calculates its position using the old high-DPI label buffer, so hide
        // that caret against the field background and draw the correct one below.
        playerNameField.setColorCursor(playerNameField.getColor().getBackground());

        int buttonWidth = 175;
        int buttonHeight = 45;
        float buttonX = sketch.width / 2f - buttonWidth / 2f;

        easyButton = addButton(cp5, "selectEasy", "easy", buttonX, 270, buttonWidth, buttonHeight);
        mediumButton = addButton(cp5, "selectMedium", "medium", buttonX, 340, buttonWidth, buttonHeight);
        hardButton = addButton(cp5, "selectHard", "hard", buttonX, 410, buttonWidth, buttonHeight);
        startButton = addButton(cp5, "startGame", "start game", buttonX, 510, buttonWidth, buttonHeight);
        addButton(cp5, "gameConfigMainMenu", "main menu", buttonX, 600, buttonWidth, buttonHeight);
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

    public String getValidPlayerName() {
        String playerName = playerNameField.getText().trim();
        if (playerName.isEmpty()) {
            validationMessage = "enter a player name";
            return null;
        }
        if (playerName.length() > GameConfig.MAX_PLAYER_NAME_LENGTH) {
            validationMessage = "player name must be at most " + GameConfig.MAX_PLAYER_NAME_LENGTH + " characters";
            return null;
        }
        validationMessage = "";
        return playerName;
    }

    @Override
    public void show() {
        super.show();
        if (selectedDifficulty == null) {
            startButton.hide();
        }
    }

    @Override
    public void draw() {
        sketch.pushStyle();
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.textSize(22);
        sketch.fill(255);
        sketch.text("player name", sketch.width / 2f - 180, 85);
        sketch.text("choose a difficulty", sketch.width / 2f - 100, 225);
        sketch.popStyle();
    }

    @Override
    public void drawOverlay() {
        float fieldX = sketch.width / 2f - 180;
        String playerName = playerNameField.getText();

        sketch.pushStyle();
        sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
        sketch.textSize(20);
        sketch.fill(playerName.isEmpty() ? 170 : 255);
        sketch.text(playerName.isEmpty() ? "enter player name" : playerName, fieldX + 12, 141);

        if (playerNameField.isFocus() && sketch.frameCount % 60 < 30) {
            float caretX = fieldX + 13 + sketch.textWidth(playerName);
            sketch.stroke(255);
            sketch.line(caretX, 128, caretX, 154);
        }

        if (!validationMessage.isEmpty()) {
            sketch.fill(255, 105, 180);
            sketch.textSize(16);
            sketch.text(validationMessage, fieldX, 180);
        }
        sketch.popStyle();
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
