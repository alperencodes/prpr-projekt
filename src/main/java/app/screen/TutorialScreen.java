package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PImage;

public class TutorialScreen extends AbstractScreen {
    private static final int PAGE_COUNT = 8;
    private static final int PINK_BASE = 0xFFFF69B4;
    private static final int DISABLED_GREY = 0xFF555555;
    private static final float IMAGE_TOP = 65f;
    private static final float MAX_IMAGE_WIDTH = 1050f;
    private static final float MAX_IMAGE_HEIGHT = 410f;

    private static final String[] TUTORIAL_TEXTS = {
            "Willkommen im Tutorial! Hier kannst du durch die verschiedenen Seiten blättern und lernen, wie du das Spiel überhaupt spielen sollst.",
            "Unter \"settings\" kannst du als allererstes die Lautstärke und FPS-Anzeige einstellen.",
            "Sobald du auf \"KAIZEN!\" drückst, landest du schon bei der Spielkonfiguration.",
            "Hier kannst du deinen Namen eingeben und eine Schwierigkeit auswählen, wonach du auf \"start game\" klicken musst.",
            "Achtung: es geht direkt los! Nun musst du eine Taste (D/F/J/K) auf deiner Tastatur drücken (nicht halten!), sobald eine Note (die pinken Rechtecke) mit den Platzhaltern unten (D/F/J/K) übereinstimmen. Alle Noten aller Beatmaps (bzw. die \"Levels\") sind grösstenteils synchron mit dem Beat des Songs im Hintergrund.",
            "Falls du eine Note nicht triffst, siehst du die Nachricht \"Miss\". Dies beeinflusst nicht deinen totalen Punktestand \"score\", jedoch kann es deine Streak (\"combo\" bzw. \"max combo\" am Schluss) beeinflussen.",
            "Je nachdem wie exakt du die Noten triffst, erhältst du 300/200/100/50 Punkte pro Note. Damit kannst du eine Streak (\"combo\") starten.",
            "Nachdem du ein Spiel abgeschlossen hast, kannst du deinen Highscore mit dem maximalen Combo unter Menü -> \"highscores\" finden. Diese sind (zusammen mit den Spieleinstellungen) lokal gespeichert."
    };

    private final PApplet sketch;
    private final PImage[] tutorialImages = new PImage[PAGE_COUNT];
    private final Button previousButton;
    private final Button nextButton;
    private final int enabledForeground;
    private final int enabledActive;
    private int currentPage;

    public TutorialScreen(PApplet sketch, ControlP5 cp5) {
        super(cp5, "tutorialGroup");
        this.sketch = sketch;

        for (int page = 0; page < PAGE_COUNT; page++) {
            tutorialImages[page] = sketch.loadImage("images/tutorial-" + (page + 1) + ".png");
            if (tutorialImages[page] == null) {
                throw new IllegalStateException("Could not load tutorial image " + (page + 1));
            }
            resizeForDisplay(tutorialImages[page]);
        }

        addButton(cp5, "tutorialMainMenu", "main menu", 25, 657, 155, 40);

        int navigationWidth = 145;
        int navigationGap = 25;
        float navigationX = sketch.width / 2f - navigationWidth - navigationGap / 2f;
        previousButton = addButton(cp5, "tutorialPrev", "prev", navigationX, 655, navigationWidth, 42);
        nextButton = addButton(cp5, "tutorialNext", "next",
                navigationX + navigationWidth + navigationGap, 655, navigationWidth, 42);
        enabledForeground = previousButton.getColor().getForeground();
        enabledActive = previousButton.getColor().getActive();
        updateNavigationButtons();
    }

    @Override
    public void show() {
        currentPage = 0;
        updateNavigationButtons();
        super.show();
    }

    @Override
    public void draw() {
        PImage image = tutorialImages[currentPage];
        float imageX = (sketch.width - image.width) / 2f;

        sketch.pushStyle();
        sketch.imageMode(PApplet.CORNER);
        sketch.image(image, imageX, IMAGE_TOP);

        sketch.fill(255);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.textSize(18);
        sketch.text(TUTORIAL_TEXTS[currentPage], 90, 495, sketch.width - 180, 130);

        sketch.textAlign(PApplet.RIGHT, PApplet.TOP);
        sketch.textSize(20);
        sketch.text("Tutorial " + (currentPage + 1) + " / " + PAGE_COUNT, sketch.width - 25, 25);
        sketch.popStyle();
    }

    private void resizeForDisplay(PImage image) {
        float scale = Math.min(MAX_IMAGE_WIDTH / image.width, MAX_IMAGE_HEIGHT / image.height);
        int displayWidth = Math.round(image.width * scale);
        int displayHeight = Math.round(image.height * scale);
        image.resize(displayWidth, displayHeight);
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateNavigationButtons();
        }
    }

    public void nextPage() {
        if (currentPage < PAGE_COUNT - 1) {
            currentPage++;
            updateNavigationButtons();
        }
    }

    private Button addButton(ControlP5 cp5, String name, String label,
                             float x, float y, int width, int height) {
        Button button = cp5.addButton(name)
                .setLabel(label)
                .setPosition(x, y)
                .setSize(width, height)
                .setColorBackground(PINK_BASE)
                .moveTo(group);
        button.getCaptionLabel()
                .toUpperCase(false)
                .setSize(20)
                .align(ControlP5.CENTER, ControlP5.CENTER);
        return button;
    }

    private void updateNavigationButtons() {
        updateButtonState(previousButton, currentPage == 0);
        updateButtonState(nextButton, currentPage == PAGE_COUNT - 1);
    }

    private void updateButtonState(Button button, boolean disabled) {
        button.setColorBackground(disabled ? DISABLED_GREY : PINK_BASE);
        button.setColorForeground(disabled ? DISABLED_GREY : enabledForeground);
        button.setColorActive(disabled ? DISABLED_GREY : enabledActive);
    }
}
