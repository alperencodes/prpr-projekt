package app.screen;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Toggle;
import processing.core.PApplet;
import settings.GameSettings;

public class SettingsScreen extends AbstractScreen {
    private static final int PINK_BASE = 0xFFFF69B4;

    private final PApplet sketch;
    private final GameSettings gameSettings;
    private final Slider songVolumeSlider;
    private final Toggle showFpsToggle;

    public SettingsScreen(PApplet sketch, ControlP5 cp5, GameSettings gameSettings) {
        super(cp5, "settingsGroup");
        this.sketch = sketch;
        this.gameSettings = gameSettings;

        float x = sketch.width / 2f - 180;
        cp5.addTextarea("songVolumeLabel")
                .setPosition(x, 145)
                .setSize(360, 35)
                .setText("song volume")
                .hideScrollbar()
                .moveTo(group);

        songVolumeSlider = cp5.addSlider("songVolume")
                .setBroadcast(false)
                .setPosition(x, 190)
                .setSize(360, 35)
                .setRange(0, 100)
                .setDecimalPrecision(0)
                .moveTo(group);
        songVolumeSlider.getCaptionLabel().hide();
        songVolumeSlider.getValueLabel().hide();

        showFpsToggle = cp5.addToggle("showFps")
                .setBroadcast(false)
                .setPosition(x, 280)
                .setSize(55, 32)
                .moveTo(group);
        showFpsToggle.getCaptionLabel().hide();

        cp5.addTextarea("showFpsLabel")
                .setPosition(x + 75, 279)
                .setSize(285, 35)
                .setText("show fps")
                .hideScrollbar()
                .moveTo(group);

        addButton(cp5, "resetSettings", "reset to defaults", x, 380, 360, 45);
        addButton(cp5, "returnToMenu", "main menu", x, 450, 360, 45);

        syncControlsFromSettings();
    }

    @Override
    public void show() {
        syncControlsFromSettings();
        super.show();
    }

    @Override
    public void draw() {
        sketch.fill(255);
        sketch.textAlign(PApplet.LEFT, PApplet.CENTER);
        sketch.text(Math.round(gameSettings.getSongVolume() * 100f) + "%", sketch.width / 2f + 195, 207);
    }

    public void syncControlsFromSettings() {
        songVolumeSlider.setBroadcast(false);
        songVolumeSlider.setValue(gameSettings.getSongVolume() * 100f);
        songVolumeSlider.setBroadcast(true);

        showFpsToggle.setBroadcast(false);
        showFpsToggle.setValue(gameSettings.isShowFps());
        showFpsToggle.setBroadcast(true);
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
                .setSize(22)
                .align(ControlP5.CENTER, ControlP5.CENTER);
    }
}
