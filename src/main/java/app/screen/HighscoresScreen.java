package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Textarea;
import highscore.Highscore;
import highscore.HighscoreRepository;
import processing.core.PApplet;

import java.util.List;

public class HighscoresScreen extends AbstractScreen {
    private static final int PINK_BASE = 0xFFFF69B4;
    private static final int TABLE_WIDTH = 900;
    private static final int TABLE_HEIGHT = 480;

    private final HighscoreRepository repository;
    private final Textarea leaderboard;

    public HighscoresScreen(PApplet sketch, ControlP5 cp5, HighscoreRepository repository) {
        super(cp5, "highscoresGroup");
        this.repository = repository;
        leaderboard = cp5.addTextarea("leaderboard")
                .setPosition((sketch.width - TABLE_WIDTH) / 2f, 70)
                .setSize(TABLE_WIDTH, TABLE_HEIGHT)
                .hideScrollbar()
                .moveTo(group);

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
        refresh();
        super.show();
    }

    private void refresh() {
        List<Highscore> highscores = repository.findTop(10);
        StringBuilder text = new StringBuilder("highscores\n\n");
        text.append(String.format("%-4s %-20s %-12s %10s %12s%n", "#", "Player", "Difficulty", "Score", "Max combo"));
        for (int i = 0; i < highscores.size(); i++) {
            Highscore score = highscores.get(i);
            text.append(String.format("%-4d %-20s %-12s %10d %12d%n",
                    i + 1, score.playerName(), score.difficulty(), score.score(), score.maxCombo()));
        }
        if (highscores.isEmpty()) {
            text.append("no completed games yet");
        }
        leaderboard.setText(text.toString());
    }
}
