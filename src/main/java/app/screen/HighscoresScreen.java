package app.screen;

import controlP5.ControlP5;
import controlP5.Textarea;
import highscore.Highscore;
import highscore.HighscoreRepository;
import processing.core.PApplet;

import java.util.List;

public class HighscoresScreen extends AbstractScreen {
    private final HighscoreRepository repository;
    private final Textarea leaderboard;

    public HighscoresScreen(PApplet sketch, ControlP5 cp5, HighscoreRepository repository) {
        super(cp5, "highscoresGroup");
        this.repository = repository;
        leaderboard = cp5.addTextarea("leaderboard")
                .setPosition(sketch.width / 2f - 390, 90)
                .setSize(780, 530)
                .hideScrollbar()
                .moveTo(group);
    }

    @Override
    public void show() {
        refresh();
        super.show();
    }

    private void refresh() {
        List<Highscore> highscores = repository.findTop(10);
        StringBuilder text = new StringBuilder("HIGHSCORES\n\n");
        text.append(String.format("%-4s %-20s %-12s %10s %12s%n", "#", "Player", "Difficulty", "Score", "Max combo"));
        for (int i = 0; i < highscores.size(); i++) {
            Highscore score = highscores.get(i);
            text.append(String.format("%-4d %-20s %-12s %10d %12d%n",
                    i + 1, score.playerName(), score.difficulty(), score.score(), score.maxCombo()));
        }
        if (highscores.isEmpty()) {
            text.append("No completed games yet.");
        }
        leaderboard.setText(text.toString());
    }
}
