package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import highscore.Highscore;
import highscore.HighscoreRepository;
import processing.core.PApplet;

import java.util.List;

public class HighscoresScreen extends AbstractScreen {
    private static final int PINK_BASE = 0xFFFF69B4;
    private static final int TABLE_MAX_WIDTH = 1000;

    private final PApplet sketch;
    private final HighscoreRepository repository;
    private List<Highscore> highscores = List.of();

    public HighscoresScreen(PApplet sketch, ControlP5 cp5, HighscoreRepository repository) {
        super(cp5, "highscoresGroup");
        this.sketch = sketch;
        this.repository = repository;

        int buttonWidth = 175;
        Button mainMenuButton = cp5.addButton("highscoresMainMenu")
                .setLabel("main menu")
                .setPosition((sketch.width - buttonWidth) / 2f, 610)
                .setSize(buttonWidth, 45)
                .setColorBackground(PINK_BASE)
                .moveTo(group);
        mainMenuButton.getCaptionLabel()
                .toUpperCase(false)
                .setSize(24)
                .align(ControlP5.CENTER, ControlP5.CENTER);
    }

    @Override
    public void show() {
        highscores = repository.findTop(10);
        super.show();
    }

    @Override
    public void draw() {
        float tableWidth = Math.min(TABLE_MAX_WIDTH, sketch.width - 120f);
        float left = (sketch.width - tableWidth) / 2f;
        float difficultyX = left + tableWidth * 0.43f;
        float scoreX = left + tableWidth * 0.75f;
        float comboX = left + tableWidth;

        sketch.pushStyle();
        sketch.fill(255);
        sketch.textSize(22);
        drawRow("Player", "Difficulty", "Score", "Max combo",
                left, difficultyX, scoreX, comboX, 105);

        if (highscores.isEmpty()) {
            sketch.textAlign(PApplet.CENTER, PApplet.TOP);
            sketch.textSize(19);
            sketch.text("no completed games yet", sketch.width / 2f, 180);
        } else {
            sketch.textSize(19);
            float rowY = 150;
            for (Highscore highscore : highscores) {
                drawRow(highscore.playerName(), highscore.difficulty().toString(),
                        Integer.toString(highscore.score()), Integer.toString(highscore.maxCombo()),
                        left, difficultyX, scoreX, comboX, rowY);
                rowY += 36;
            }
        }
        sketch.popStyle();
    }

    private void drawRow(String player, String difficulty, String score, String maxCombo,
                         float playerX, float difficultyX, float scoreX, float comboX, float y) {
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.text(player, playerX, y);
        sketch.text(difficulty, difficultyX, y);

        sketch.textAlign(PApplet.RIGHT, PApplet.TOP);
        sketch.text(score, scoreX, y);
        sketch.text(maxCombo, comboX, y);
    }
}
