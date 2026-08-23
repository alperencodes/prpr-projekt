package app.screen;

import app.MainSketch;
import controlP5.Button;
import controlP5.ControlP5;

public class MenuScreen extends AbstractScreen {
    private static final int PINK_BASE = 0xFFFF69B4;
    private static final int PINK_ACCENT = 0xFFFF1493;

    private final MainSketch sketch;
    private final Button titleButton;

    public MenuScreen(MainSketch sketch, ControlP5 cp5) {
        super(cp5, "menuGroup");
        this.sketch = sketch;

        cp5.addTextarea("welcomeMessage")
                .setPosition(sketch.width / 2f - 100, 120)
                .setSize(300, 40)
                .setText("welcome to...")
                .hideScrollbar()
                .moveTo(group);

        int buttonWidth = 175;
        int buttonHeight = 40;
        float buttonX = sketch.width / 2f - buttonWidth / 2f;

        int titleButtonWidth = 210;
        int titleButtonHeight = 60;
        float titleButtonX = sketch.width / 2f - titleButtonWidth / 2f;

        titleButton = cp5.addButton("gameConfig")
                .setLabel("KAIZEN!")
                .setPosition(titleButtonX, 175)
                .setSize(titleButtonWidth, titleButtonHeight)
                .setColorBackground(PINK_BASE)
                .moveTo(group);
        titleButton.getCaptionLabel()
                .toUpperCase(false)
                .setSize(40)
                .align(ControlP5.CENTER, ControlP5.CENTER);

        addButton(cp5, "Highscores", "highscores", buttonX, 300, buttonWidth, buttonHeight);
        addButton(cp5, "Tutorial", "tutorial", buttonX, 375, buttonWidth, buttonHeight);
        addButton(cp5, "Settings", "settings", buttonX, 450, buttonWidth, buttonHeight);
        addButton(cp5, "Exit", "exit", buttonX, 525, buttonWidth, buttonHeight);
    }

    @Override
    public void draw() {
        float wave = sketch.sin(sketch.millis() * 0.003f);
        float interpolationFactor = sketch.map(wave, -1f, 1f, 0f, 1f);
        int dynamicPink = sketch.lerpColor(PINK_BASE, PINK_ACCENT, interpolationFactor);

        titleButton.setColorBackground(dynamicPink);
    }

    private void addButton(ControlP5 cp5, String name, String label, float x, int y, int width, int height) {
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
    }
}
